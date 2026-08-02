package com.isaacshub.app.homecontrol.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Repository for Home Control feature - Matter-enabled smart home device control.
 * Manages devices, rooms, routines, and favorites with in-memory caching.
 */
class HomeControlRepository(private val apiClient: HomeControlApiClient) {

    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    val devices: StateFlow<List<Device>> = _devices.asStateFlow()

    private val _rooms = MutableStateFlow<List<Room>>(emptyList())
    val rooms: StateFlow<List<Room>> = _rooms.asStateFlow()

    private val _routines = MutableStateFlow<List<Routine>>(emptyList())
    val routines: StateFlow<List<Routine>> = _routines.asStateFlow()

    private val _favorites = MutableStateFlow<List<String>>(emptyList())
    val favorites: StateFlow<List<String>> = _favorites.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Cache for device state to avoid unnecessary network calls
    private val deviceStateCache = mutableMapOf<String, Device>()

    // ==================== Devices ====================

    suspend fun loadDevices(): Result<Unit> {
        _isLoading.value = true
        _error.value = null
        return try {
            val result = apiClient.getDevices()
            result.fold(
                onSuccess = { devicesList ->
                    _devices.value = devicesList
                    // Update cache
                    devicesList.forEach { device ->
                        deviceStateCache[device.id] = device
                    }
                    Result.success(Unit)
                },
                onFailure = { exception ->
                    _error.value = exception.message
                    Result.failure(exception)
                }
            )
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun discoverDevices(): Result<DeviceDiscoveryResponse> {
        _isLoading.value = true
        _error.value = null
        return try {
            val result = apiClient.discoverDevices()
            result.fold(
                onSuccess = { response ->
                    // Reload devices after discovery
                    loadDevices()
                    Result.success(response)
                },
                onFailure = { exception ->
                    _error.value = exception.message
                    Result.failure(exception)
                }
            )
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun sendDeviceCommand(deviceId: String, capability: String, value: Any): Result<Device> {
        _error.value = null
        return apiClient.sendDeviceCommand(deviceId, capability, value).also { result ->
            result.onSuccess { updatedDevice ->
                // Update cache
                deviceStateCache[deviceId] = updatedDevice
                // Update devices list
                _devices.value = _devices.value.map { device ->
                    if (device.id == deviceId) updatedDevice else device
                }
            }
            result.onFailure { exception ->
                _error.value = exception.message
            }
        }
    }

    fun getDeviceById(deviceId: String): Device? {
        return deviceStateCache[deviceId] ?: _devices.value.find { it.id == deviceId }
    }

    fun getDevicesByRoom(roomId: String): List<Device> {
        return _devices.value.filter { it.roomId == roomId }
    }

    fun getFavoriteDevices(): List<Device> {
        return _devices.value.filter { it.isFavorite }
    }

    fun getOnlineDevices(): List<Device> {
        return _devices.value.filter { it.online }
    }

    fun getDevicesByType(type: DeviceType): List<Device> {
        return _devices.value.filter { it.type == type }
    }

    // ==================== Rooms ====================

    suspend fun loadRooms(): Result<Unit> {
        _isLoading.value = true
        _error.value = null
        return try {
            val result = apiClient.getRooms()
            result.fold(
                onSuccess = { roomsList ->
                    _rooms.value = roomsList.sortedBy { it.order }
                    Result.success(Unit)
                },
                onFailure = { exception ->
                    _error.value = exception.message
                    Result.failure(exception)
                }
            )
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun createRoom(room: Room): Result<Room> {
        _error.value = null
        return apiClient.createRoom(room).also { result ->
            result.onSuccess {
                loadRooms()
            }
            result.onFailure { exception ->
                _error.value = exception.message
            }
        }
    }

    suspend fun updateRoom(room: Room): Result<Room> {
        _error.value = null
        return apiClient.updateRoom(room).also { result ->
            result.onSuccess {
                loadRooms()
            }
            result.onFailure { exception ->
                _error.value = exception.message
            }
        }
    }

    suspend fun deleteRoom(roomId: String): Result<Boolean> {
        _error.value = null
        return apiClient.deleteRoom(roomId).also { result ->
            result.onSuccess {
                loadRooms()
                // Also reload devices since their roomId may have changed
                loadDevices()
            }
            result.onFailure { exception ->
                _error.value = exception.message
            }
        }
    }

    fun getRoomById(roomId: String): Room? {
        return _rooms.value.find { it.id == roomId }
    }

    // ==================== Routines ====================

    suspend fun loadRoutines(): Result<Unit> {
        _isLoading.value = true
        _error.value = null
        return try {
            val result = apiClient.getRoutines()
            result.fold(
                onSuccess = { routinesList ->
                    _routines.value = routinesList
                    Result.success(Unit)
                },
                onFailure = { exception ->
                    _error.value = exception.message
                    Result.failure(exception)
                }
            )
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun createRoutine(routine: Routine): Result<Routine> {
        _error.value = null
        return apiClient.createRoutine(routine).also { result ->
            result.onSuccess {
                loadRoutines()
            }
            result.onFailure { exception ->
                _error.value = exception.message
            }
        }
    }

    suspend fun updateRoutine(routine: Routine): Result<Routine> {
        _error.value = null
        return apiClient.updateRoutine(routine).also { result ->
            result.onSuccess {
                loadRoutines()
            }
            result.onFailure { exception ->
                _error.value = exception.message
            }
        }
    }

    suspend fun deleteRoutine(routineId: String): Result<Boolean> {
        _error.value = null
        return apiClient.deleteRoutine(routineId).also { result ->
            result.onSuccess {
                loadRoutines()
            }
            result.onFailure { exception ->
                _error.value = exception.message
            }
        }
    }

    suspend fun executeRoutine(routineId: String): Result<RoutineExecutionResponse> {
        _error.value = null
        return apiClient.executeRoutine(routineId).also { result ->
            result.onSuccess {
                // Reload devices after routine execution to reflect state changes
                loadDevices()
                // Also reload routines to update lastRunAt and runCount
                loadRoutines()
            }
            result.onFailure { exception ->
                _error.value = exception.message
            }
        }
    }

    suspend fun toggleRoutineEnabled(routine: Routine): Result<Routine> {
        return updateRoutine(routine.copy(enabled = !routine.enabled))
    }

    fun getRoutineById(routineId: String): Routine? {
        return _routines.value.find { it.id == routineId }
    }

    fun getEnabledRoutines(): List<Routine> {
        return _routines.value.filter { it.enabled }
    }

    fun getManualRoutines(): List<Routine> {
        return _routines.value.filter { routine ->
            routine.triggers.any { it.type == TriggerType.MANUAL }
        }
    }

    // ==================== Favorites ====================

    suspend fun loadFavorites(): Result<Unit> {
        _isLoading.value = true
        _error.value = null
        return try {
            val result = apiClient.getFavorites()
            result.fold(
                onSuccess = { favoritesList ->
                    _favorites.value = favoritesList
                    Result.success(Unit)
                },
                onFailure = { exception ->
                    _error.value = exception.message
                    Result.failure(exception)
                }
            )
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun updateFavorites(deviceIds: List<String>): Result<List<String>> {
        _error.value = null
        return apiClient.updateFavorites(deviceIds).also { result ->
            result.onSuccess { favoritesList ->
                _favorites.value = favoritesList
                // Reload devices to update isFavorite flag
                loadDevices()
            }
            result.onFailure { exception ->
                _error.value = exception.message
            }
        }
    }

    suspend fun toggleFavorite(deviceId: String): Result<List<String>> {
        val currentFavorites = _favorites.value.toMutableList()
        val newFavorites = if (currentFavorites.contains(deviceId)) {
            currentFavorites - deviceId
        } else {
            currentFavorites + deviceId
        }
        return updateFavorites(newFavorites)
    }

    // ==================== Utility Methods ====================

    suspend fun refresh() {
        loadDevices()
        loadRooms()
        loadRoutines()
        loadFavorites()
    }

    fun clearError() {
        _error.value = null
    }

    fun clearCache() {
        deviceStateCache.clear()
        _devices.value = emptyList()
        _rooms.value = emptyList()
        _routines.value = emptyList()
        _favorites.value = emptyList()
    }

    // ==================== Helper Methods for UI ====================

    /**
     * Get all devices organized by rooms, plus unassigned devices
     */
    fun getDevicesGroupedByRoom(): Map<Room?, List<Device>> {
        val devicesByRoom = mutableMapOf<Room?, List<Device>>()

        // Group devices by room
        _rooms.value.forEach { room ->
            devicesByRoom[room] = getDevicesByRoom(room.id)
        }

        // Add unassigned devices
        val unassignedDevices = _devices.value.filter { it.roomId == null }
        if (unassignedDevices.isNotEmpty()) {
            devicesByRoom[null] = unassignedDevices
        }

        return devicesByRoom
    }

    /**
     * Check if a device supports a specific capability
     */
    fun deviceSupportsCapability(deviceId: String, capability: Capability): Boolean {
        return getDeviceById(deviceId)?.capabilities?.contains(capability) ?: false
    }

    /**
     * Get the current state value for a device capability
     */
    fun getDeviceCapabilityValue(deviceId: String, capability: Capability): Any? {
        val device = getDeviceById(deviceId) ?: return null
        return device.state[capability.toApiString()]
    }

    /**
     * Get all devices that can be controlled (have POWER or other control capabilities)
     */
    fun getControllableDevices(): List<Device> {
        return _devices.value.filter { device ->
            device.capabilities.any { capability ->
                capability in listOf(
                    Capability.POWER,
                    Capability.BRIGHTNESS,
                    Capability.COLOR_TEMPERATURE,
                    Capability.COLOR,
                    Capability.TEMPERATURE_SETPOINT,
                    Capability.LOCK
                )
            }
        }
    }

    /**
     * Get all devices that are sensors (read-only)
     */
    fun getSensorDevices(): List<Device> {
        return _devices.value.filter { device ->
            device.type == DeviceType.SENSOR ||
            device.capabilities.any { capability ->
                capability in listOf(
                    Capability.TEMPERATURE_MEASUREMENT,
                    Capability.OCCUPANCY,
                    Capability.CONTACT,
                    Capability.MOTION,
                    Capability.HUMIDITY,
                    Capability.BATTERY
                )
            }
        }
    }
}
