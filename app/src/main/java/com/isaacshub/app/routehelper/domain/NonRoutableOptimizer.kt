package com.isaacshub.app.routehelper.domain

import com.isaacshub.app.routehelper.data.RoutedStopEntity

/**
 * Optimizer for non-routable package placement.
 *
 * Non-routable packages are those without a sequence number from the clerk.
 * This optimizer finds the best location to place them between existing routable stops
 * by calculating which gap would require the shortest detour in the overall route.
 */
object NonRoutableOptimizer {

    /**
     * Find the best position to insert a non-routable address with minimal route deviation.
     *
     * @param nonRoutableLocation The GeoPoint of the non-routable package
     * @param routeStops All existing routed stops (regular stops only, sorted by sequence)
     * @return Triple of (stopBefore, stopAfter, floatSequenceNumber)
     *         - stopBefore: the stop to insert before, null if inserting at start
     *         - stopAfter: the stop to insert after, null if inserting at end
     *         - floatSequenceNumber: the sequence number to assign (e.g., 22.5 for between 22 and 23)
     */
    fun findOptimalInsertionPoint(
        nonRoutableLocation: GeoPoint,
        routeStops: List<RoutedStopEntity>
    ): Triple<RoutedStopEntity?, RoutedStopEntity?, Float> {

        if (routeStops.isEmpty()) {
            // No existing stops - this is the first stop
            return Triple(null, null, 1.0f)
        }

        if (routeStops.size == 1) {
            // Only one stop - check if non-routable is closer to it or should go before
            val stop = routeStops[0]
            val distance = distanceMeters(nonRoutableLocation, stop.toGeoPoint())

            // If very close to the existing stop, insert before it
            // Otherwise it depends on other factors, but we'll default to before
            return Triple(null, stop, 0.5f)
        }

        // Start with default position: before first stop
        var bestPosition: Triple<RoutedStopEntity?, RoutedStopEntity?, Float> =
            Triple(null, routeStops[0], 0.5f)
        var bestDetour = calculateDetour(
            nonRoutableLocation,
            null,
            routeStops[0]
        )

        // Test each gap between consecutive stops
        for (i in 0 until routeStops.size - 1) {
            val stopBefore = routeStops[i]
            val stopAfter = routeStops[i + 1]

            val detour = calculateDetour(
                nonRoutableLocation,
                stopBefore,
                stopAfter
            )

            if (detour < bestDetour) {
                bestDetour = detour
                val sequenceNumber = stopBefore.sequenceOrder + 0.5f
                bestPosition = Triple(stopBefore, stopAfter, sequenceNumber)
            }
        }

        // Also test inserting after the last stop
        val lastStop = routeStops.last()
        val distanceToLast = distanceMeters(nonRoutableLocation, lastStop.toGeoPoint())
        if (distanceToLast < bestDetour) {
            val sequenceNumber = lastStop.sequenceOrder + 1.0f
            bestPosition = Triple(lastStop, null, sequenceNumber)
        }

        return bestPosition
    }

    /**
     * Calculate the additional distance required to visit the non-routable location
     * between two existing stops (or before the first/after the last).
     *
     * Direct route: Stop A -> Stop B = distance(A, B)
     * With non-routable: Stop A -> Non-routable -> Stop B = distance(A, NR) + distance(NR, B)
     * Detour = (A->NR + NR->B) - (A->B)
     *
     * @param nonRoutableLocation The location of the non-routable package
     * @param stopBefore The stop before the insertion point (null if inserting at start)
     * @param stopAfter The stop after the insertion point (null if inserting at end)
     * @return The additional distance (detour) in meters
     */
    private fun calculateDetour(
        nonRoutableLocation: GeoPoint,
        stopBefore: RoutedStopEntity?,
        stopAfter: RoutedStopEntity?
    ): Double {
        return when {
            stopBefore == null && stopAfter != null -> {
                // Inserting before the first stop: just the distance to that stop
                distanceMeters(nonRoutableLocation, stopAfter.toGeoPoint())
            }
            stopBefore != null && stopAfter == null -> {
                // Inserting after the last stop: just the distance from that stop
                distanceMeters(stopBefore.toGeoPoint(), nonRoutableLocation)
            }
            stopBefore != null && stopAfter != null -> {
                // Inserting between two stops
                val directDistance = distanceMeters(stopBefore.toGeoPoint(), stopAfter.toGeoPoint())
                val detouredDistance = distanceMeters(stopBefore.toGeoPoint(), nonRoutableLocation) +
                        distanceMeters(nonRoutableLocation, stopAfter.toGeoPoint())
                detouredDistance - directDistance
            }
            else -> Double.MAX_VALUE  // Should not happen
        }
    }

    /**
     * Re-optimize placement of all non-routable stops.
     * Called when entering play mode or after new regular stops are added.
     *
     * @param allStops All stops (both routable and non-routable)
     * @return All stops sorted by sequence order, with non-routables re-positioned
     */
    fun reoptimizeAllNonRoutables(
        allStops: List<RoutedStopEntity>
    ): List<RoutedStopEntity> {
        val regularStops = allStops.filter { !it.isNonRoutable }.sortedBy { it.sequenceOrder }
        val nonRoutables = allStops.filter { it.isNonRoutable }

        if (nonRoutables.isEmpty()) {
            // No non-routables to optimize
            return allStops.sortedBy { it.sequenceOrder }
        }

        val optimizedNonRoutables = nonRoutables.map { nonRoutable ->
            val (_, _, newSequence) = findOptimalInsertionPoint(
                nonRoutable.toGeoPoint(),
                regularStops
            )
            nonRoutable.copy(sequenceOrder = newSequence)
        }

        // Merge and re-sort all stops
        return (regularStops + optimizedNonRoutables).sortedBy { it.sequenceOrder }
    }
}

/** Extension function to convert a RoutedStopEntity to a GeoPoint */
fun RoutedStopEntity.toGeoPoint(): GeoPoint = GeoPoint(latitude, longitude)
