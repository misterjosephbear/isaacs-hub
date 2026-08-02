package com.isaacshub.app.homecontrol.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.isaacshub.app.homecontrol.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeControlUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val devices: List<Device> = emptyList(),
    val rooms: List<Room> = emptyList(),
    val routines: List<Routine> = emptyList(),
    val favorites: List<String> = emptyList()
) {
    val favoriteDevices: List<Device>
        get() = devices.filter { it.isFavorite }

    val onlineDevices: List<Device>
        get() = devices.filter { it.online }

    val offlineDevices: List<Device>
        get() = devices.filter { !it.online }

    val devicesGroupedByRoom: Map<Room?, List<Device>>
        get() {
            val result = mutableMapOf<Room?, List<Device>>()
            rooms.forEach { room ->
                result[room] = devices.filter { it.roomId == room.id }
            }
            val unassigned = devices.filter { it.roomId == null }
            if (unassigned.isNotEmpty()) {
                result[null] = unassigned
            }
            return result
        }
}

class HomeControlViewModel(
    private val repository: HomeControlRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeControlUiState())
    val uiState: StateFlow<HomeControlUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Combine all flows into UI state
            combine(
                repository.devices,
                repository.rooms,
                repository.routines,
                repository.favorites,
                repository.isLoading,
                repository.error
            ) { flows: Array<*> ->
                HomeControlUiState(
                    isLoading = flows[4] as Boolean,
                    error = flows[5] as String?,
                    devices = flows[0] as List<Device>,
                    rooms = flows[1] as List<Room>,
                    routines = flows[2] as List<Routine>,
                    favorites = flows[3] as List<String>
                )
            }.collect { state ->
                _uiState.value = state
            }
        }

        // Initial load
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            repository.refresh()
        }
    }

    fun discoverDevices() {
        viewModelScope.launch {
            repository.discoverDevices()
        }
    }

    fun toggleDevicePower(deviceId: String) {
        viewModelScope.launch {
            val device = repository.getDeviceById(deviceId) ?: return@launch
            val currentPower = device.state.getPower() ?: false
            repository.sendDeviceCommand(deviceId, "power", !currentPower)
        }
    }

    fun sendDeviceCommand(deviceId: String, capability: String, value: Any) {
        viewModelScope.launch {
            repository.sendDeviceCommand(deviceId, capability, value)
        }
    }

    fun executeRoutine(routineId: String) {
        viewModelScope.launch {
            repository.executeRoutine(routineId)
        }
    }

    fun toggleRoutineEnabled(routine: Routine) {
        viewModelScope.launch {
            repository.toggleRoutineEnabled(routine)
        }
    }

    fun toggleFavorite(deviceId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(deviceId)
        }
    }

    fun clearError() {
        repository.clearError()
    }

    class Factory(
        private val repository: HomeControlRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            HomeControlViewModel(repository) as T
    }
}
