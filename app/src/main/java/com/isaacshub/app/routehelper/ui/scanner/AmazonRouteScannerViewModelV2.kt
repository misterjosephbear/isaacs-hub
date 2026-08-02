package com.isaacshub.app.routehelper.ui.scanner

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.isaacshub.app.App
import com.isaacshub.app.routehelper.data.CandidateAddressEntity
import com.isaacshub.app.routehelper.data.ScannedAddressData
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
    val sequenceNumber: Int,  // 1-based display number
    val matchedCandidateId: Long?,
    val packageCount: Int = 1  // Default to 1 package per stop
)

/**
 * UI state for the enhanced scanner V2
 */
data class AmazonScannerV2UiState(
    val scannedStops: List<ScannedStopV2> = emptyList(),
    val candidateAddresses: List<CandidateAddressEntity> = emptyList(),
    val isLoading: Boolean = true,
    val validationMessage: String? = null,
    val isCreatingStops: Boolean = false,
    val error: String? = null
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

        // Create new stop
        val nextSequence = _uiState.value.scannedStops.size + 1
        val newStop = ScannedStopV2(
            addressLabel = addressLabel,
            sequenceNumber = nextSequence,
            matchedCandidateId = matchedCandidateId,
            packageCount = 1  // Default to 1 package
        )

        _uiState.update { state ->
            state.copy(
                scannedStops = state.scannedStops + newStop,
                validationMessage = "✓ Added: $addressLabel"
            )
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
     * Remove a stop from the list
     */
    fun removeStop(stop: ScannedStopV2) {
        _uiState.update { state ->
            val updatedList = state.scannedStops.filter { it.sequenceNumber != stop.sequenceNumber }
            // Renumber remaining stops
            val renumbered = updatedList.mapIndexed { index, s ->
                s.copy(sequenceNumber = index + 1)
            }
            state.copy(scannedStops = renumbered)
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
                        expectedPackageCount = stop.packageCount
                    )
                }

                repository.createStopsFromScannedAddresses(routeId, scannedData)
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
