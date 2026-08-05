package com.isaacshub.app.routehelper.ui.scanner

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.isaacshub.app.App
import com.isaacshub.app.routehelper.data.CandidateAddressEntity
import com.isaacshub.app.routehelper.data.ScannedAddressData
import com.isaacshub.app.routehelper.data.RoutedStopEntity
import com.isaacshub.app.routehelper.domain.GeoPoint
import com.isaacshub.app.routehelper.domain.NonRoutableOptimizer
import com.isaacshub.app.routehelper.domain.toGeoPoint
import com.isaacshub.app.routehelper.util.normalizeAddress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Data class for a scanned stop in the V2 scanner
 */
data class ScannedStopV2(
    val addressLabel: String,
    val sequenceNumber: Float,  // 1-based display number (can be fractional for non-routables like 22.5)
    val matchedCandidateId: Long?,
    val packageCount: Int = 1,  // Default to 1 package per stop
    val isNonRoutable: Boolean = false  // True if this is a non-routable package
)

/**
 * Scanning mode for the V2 scanner
 */
enum class ScanMode {
    REGULAR_STOPS,      // Scanning for regular stops (only green/matched addresses allowed)
    NON_ROUTABLES       // Scanning for non-routable packages (any address allowed)
}

/**
 * UI state for the enhanced scanner V2
 */
data class AmazonScannerV2UiState(
    val scannedStops: List<ScannedStopV2> = emptyList(),
    val candidateAddresses: List<CandidateAddressEntity> = emptyList(),
    val isLoading: Boolean = true,
    val validationMessage: String? = null,
    val isCreatingStops: Boolean = false,
    val error: String? = null,
    val scanMode: ScanMode = ScanMode.REGULAR_STOPS  // Current scanning mode
)

/**
 * ViewModel for the enhanced Amazon Route Scanner V2
 */
class AmazonRouteScannerViewModelV2(
    application: Application
) : AndroidViewModel(application) {

    private val repository = getApplication<App>().routeHelperRepository

    private val _uiState = MutableStateFlow(AmazonScannerV2UiState())
    val uiState: StateFlow<AmazonScannerV2UiState> = _uiState.asStateFlow()

    private var currentRouteId: Long? = null

    fun start(routeId: Long) {
        if (currentRouteId == routeId) return  // Already started
        currentRouteId = routeId
        loadCandidateAddresses(routeId)
    }

    private fun loadCandidateAddresses(routeId: Long) {
        viewModelScope.launch {
            try {
                val candidates = mutableListOf<CandidateAddressEntity>()
                repository.observeUnroutedCandidates(routeId).collect { list ->
                    candidates.clear()
                    candidates.addAll(list)
                    _uiState.update { it.copy(candidateAddresses = list, isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = "Failed to load addresses: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    /**
     * Set the scanning mode (regular stops or non-routables)
     */
    fun setScanMode(mode: ScanMode) {
        _uiState.update { it.copy(scanMode = mode) }
    }

    /**
     * Add a stop from a detected address (called when user taps on green bounding box)
     */
    fun addStopFromAddress(detectedAddress: DetectedAddress) {
        val matchedCandidateId = detectedAddress.matchedCandidateId
        val addressLabel = detectedAddress.matchedLabel ?: detectedAddress.text

        if (matchedCandidateId == null) {
            _uiState.update {
                it.copy(validationMessage = "⚠ Address not matched to ZIP code")
            }
            return
        }

        // Check for duplicates
        val normalizedScanned = normalizeAddress(addressLabel)
        val isDuplicate = _uiState.value.scannedStops.any { stop ->
            normalizeAddress(stop.addressLabel) == normalizedScanned
        }

        if (isDuplicate) {
            _uiState.update {
                it.copy(validationMessage = "⚠ Duplicate address - already added")
            }
            return
        }

        // Create new stop with sequential numbering
        val regularStops = _uiState.value.scannedStops.filter { !it.isNonRoutable }
        val nextSequence = regularStops.size + 1
        val newStop = ScannedStopV2(
            addressLabel = addressLabel,
            sequenceNumber = nextSequence.toFloat(),
            matchedCandidateId = matchedCandidateId,
            packageCount = 1,  // Default to 1 package
            isNonRoutable = false
        )

        _uiState.update { state ->
            state.copy(
                scannedStops = state.scannedStops + newStop,
                validationMessage = "✓ Added: $addressLabel"
            )
        }

        // Re-optimize non-routables if any exist after adding new regular stop
        reoptimizeNonRoutablesIfNeeded()
    }

    /**
     * Add a non-routable address from a detected address.
     * Non-routables will be intelligently placed between existing stops with minimal route detour
     * when the route is saved to the database (finishScanning).
     * During scanning, they're just assigned a temporary sequence and will be optimized later.
     */
    fun addNonRoutableFromAddress(detectedAddress: DetectedAddress) {
        val addressLabel = detectedAddress.matchedLabel ?: detectedAddress.text

        // Check for duplicates
        val normalizedScanned = normalizeAddress(addressLabel)
        val isDuplicate = _uiState.value.scannedStops.any { stop ->
            normalizeAddress(stop.addressLabel) == normalizedScanned
        }

        if (isDuplicate) {
            _uiState.update {
                it.copy(validationMessage = "⚠ Duplicate address - already added")
            }
            return
        }

        // For non-routables during scanning, assign a temporary sequence at the end
        // They will be re-optimized when the route is finalized and saved to the database
        val maxSequence = _uiState.value.scannedStops.maxOfOrNull { it.sequenceNumber } ?: 0.0f
        val tempSequence = maxSequence + 1.0f

        val newStop = ScannedStopV2(
            addressLabel = addressLabel,
            sequenceNumber = tempSequence,
            matchedCandidateId = detectedAddress.matchedCandidateId,
            packageCount = 1,
            isNonRoutable = true
        )

        _uiState.update { state ->
            state.copy(
                scannedStops = state.scannedStops + newStop,
                validationMessage = "✓ Added non-routable: $addressLabel (will be re-optimized on save)"
            )
        }
    }

    /**
     * Re-optimize non-routable positions if they exist and regular stops have changed.
     * Note: This implementation is simplified for UI state management.
     * Full optimization would happen during finishScanning when we have all coordinates.
     */
    private fun reoptimizeNonRoutablesIfNeeded() {
        val nonRoutables = _uiState.value.scannedStops.filter { it.isNonRoutable }
        if (nonRoutables.isEmpty()) return

        // For now, just ensure non-routables stay sorted between regular stops
        // Real optimization will happen when stops are saved to database
        val regularStops = _uiState.value.scannedStops.filter { !it.isNonRoutable }

        // Simple re-sequencing: ensure each non-routable has an appropriate fractional position
        // This is a placeholder - real logic would need actual coordinates
        val optimizedNonRoutables = nonRoutables.map { nonRoutable ->
            // Keep it in its appropriate position relative to the nearest regular stops
            nonRoutable  // For now, just keep as-is
        }

        _uiState.update { state ->
            val newStops = (regularStops + optimizedNonRoutables).sortedBy { it.sequenceNumber }
            state.copy(scannedStops = newStops)
        }
    }

    /**
     * Increment package count for a stop
     */
    fun incrementPackageCount(stop: ScannedStopV2) {
        _uiState.update { state ->
            val updatedStops = state.scannedStops.map {
                if (it.sequenceNumber == stop.sequenceNumber) {
                    it.copy(packageCount = it.packageCount + 1)
                } else {
                    it
                }
            }
            state.copy(scannedStops = updatedStops)
        }
    }

    /**
     * Decrement package count for a stop (minimum 0)
     */
    fun decrementPackageCount(stop: ScannedStopV2) {
        _uiState.update { state ->
            val updatedStops = state.scannedStops.map {
                if (it.sequenceNumber == stop.sequenceNumber && it.packageCount > 0) {
                    it.copy(packageCount = it.packageCount - 1)
                } else {
                    it
                }
            }
            state.copy(scannedStops = updatedStops)
        }
    }

    /**
     * Remove a stop from the list (handles both regular and non-routable stops)
     */
    fun removeStop(stop: ScannedStopV2) {
        _uiState.update { state ->
            val updatedList = state.scannedStops.filter { it.sequenceNumber != stop.sequenceNumber }

            if (stop.isNonRoutable) {
                // For non-routables, just remove and keep others as-is
                state.copy(scannedStops = updatedList)
            } else {
                // For regular stops, renumber remaining regular stops sequentially
                val regularStops = updatedList.filter { !it.isNonRoutable }
                val nonRoutables = updatedList.filter { it.isNonRoutable }

                val renumberedRegular = regularStops.mapIndexed { index, s ->
                    s.copy(sequenceNumber = (index + 1).toFloat())
                }

                // Re-optimize non-routables after removing a regular stop
                val optimizedNonRoutables = nonRoutables.map { nr ->
                    // Keep non-routable but mark that it may need re-optimization
                    nr
                }

                val newStops = (renumberedRegular + optimizedNonRoutables).sortedBy { it.sequenceNumber }
                state.copy(scannedStops = newStops)
            }
        }
    }

    fun clearValidationMessage() {
        _uiState.update { it.copy(validationMessage = null) }
    }

    /**
     * Finish scanning and create all stops in the repository
     */
    fun finishScanning(onComplete: () -> Unit) {
        val routeId = currentRouteId ?: return
        val stops = _uiState.value.scannedStops
        if (stops.isEmpty()) {
            _uiState.update { it.copy(error = "Add at least one stop before finishing") }
            return
        }

        _uiState.update { it.copy(isCreatingStops = true) }

        viewModelScope.launch {
            try {
                // Convert UI model to repository model
                val scannedData = stops.map { stop ->
                    ScannedAddressData(
                        addressLabel = stop.addressLabel,
                        sequenceNumber = stop.sequenceNumber,
                        matchedCandidateId = stop.matchedCandidateId,
                        isValid = stop.matchedCandidateId != null,
                        expectedPackageCount = stop.packageCount,
                        isNonRoutable = stop.isNonRoutable
                    )
                }

                repository.createStopsFromScannedAddresses(routeId, scannedData)

                // If there are non-routable stops, optimize their placement
                if (scannedData.any { it.isNonRoutable }) {
                    repository.optimizeNonRoutableStopPlacement(routeId)
                }

                onComplete()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = "Failed to create stops: ${e.message}",
                        isCreatingStops = false
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
