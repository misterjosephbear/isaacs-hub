package com.isaacshub.app.homecontrol.data

import com.isaacshub.app.core.network.BaseApiClient
import com.isaacshub.app.vault.domain.VaultConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

/**
 * API client for Home Control feature - Matter-enabled smart home device control.
 * Communicates with isaacs-hub-server's /api/homecontrol endpoints.
 */
class HomeControlApiClient(connection: VaultConnection) : BaseApiClient(connection) {

    // ==================== Devices ====================

    suspend fun getDevices(): Result<List<Device>> = withContext(Dispatchers.IO) {
        val result = tryEachBaseUrl { baseUrl ->
            val json = get(baseUrl, "/api/homecontrol/devices")
            val devicesArray = json.getJSONArray("devices")
            List(devicesArray.length()) { i ->
                parseDevice(devicesArray.getJSONObject(i))
            }
        }
        result?.let { Result.success(it) } ?: Result.failure(Exception("Couldn't reach the server."))
    }

    suspend fun discoverDevices(): Result<DeviceDiscoveryResponse> = withContext(Dispatchers.IO) {
        val result = tryEachBaseUrl { baseUrl ->
            val json = post(baseUrl, "/api/homecontrol/devices/discover")
            parseDeviceDiscoveryResponse(json)
        }
        result?.let { Result.success(it) } ?: Result.failure(Exception("Couldn't reach the server."))
    }

    suspend fun pairDeviceWithQrCode(qrCode: String): Result<DeviceDiscoveryResponse> = withContext(Dispatchers.IO) {
        val result = tryEachBaseUrl { baseUrl ->
            val body = JSONObject().apply {
                put("qrCode", qrCode)
            }
            val json = post(baseUrl, "/api/homecontrol/devices/pair", body)
            parseDeviceDiscoveryResponse(json)
        }
        result?.let { Result.success(it) } ?: Result.failure(Exception("Couldn't reach the server."))
    }

    suspend fun sendDeviceCommand(deviceId: String, capability: String, value: Any): Result<Device> =
        withContext(Dispatchers.IO) {
            val result = tryEachBaseUrl { baseUrl ->
                val body = JSONObject().apply {
                    put("capability", capability)
                    put("value", value)
                }
                val json = post(baseUrl, "/api/homecontrol/devices/$deviceId/command", body)
                parseDevice(json.getJSONObject("device"))
            }
            result?.let { Result.success(it) } ?: Result.failure(Exception("Couldn't reach the server."))
        }

    // ==================== Rooms ====================

    suspend fun getRooms(): Result<List<Room>> = withContext(Dispatchers.IO) {
        val result = tryEachBaseUrl { baseUrl ->
            val json = get(baseUrl, "/api/homecontrol/rooms")
            val roomsArray = json.getJSONArray("rooms")
            List(roomsArray.length()) { i ->
                parseRoom(roomsArray.getJSONObject(i))
            }
        }
        result?.let { Result.success(it) } ?: Result.failure(Exception("Couldn't reach the server."))
    }

    suspend fun createRoom(room: Room): Result<Room> = withContext(Dispatchers.IO) {
        val result = tryEachBaseUrl { baseUrl ->
            val body = serializeRoom(room)
            val json = post(baseUrl, "/api/homecontrol/rooms", body)
            parseRoom(json.getJSONObject("room"))
        }
        result?.let { Result.success(it) } ?: Result.failure(Exception("Couldn't reach the server."))
    }

    suspend fun updateRoom(room: Room): Result<Room> = withContext(Dispatchers.IO) {
        val result = tryEachBaseUrl { baseUrl ->
            val body = serializeRoom(room)
            val json = put(baseUrl, "/api/homecontrol/rooms/${room.id}", body)
            parseRoom(json.getJSONObject("room"))
        }
        result?.let { Result.success(it) } ?: Result.failure(Exception("Couldn't reach the server."))
    }

    suspend fun deleteRoom(roomId: String): Result<Boolean> = withContext(Dispatchers.IO) {
        val result = tryEachBaseUrl { baseUrl ->
            delete(baseUrl, "/api/homecontrol/rooms/$roomId")
        }
        result?.let { Result.success(it) } ?: Result.failure(Exception("Couldn't reach the server."))
    }

    // ==================== Routines ====================

    suspend fun getRoutines(): Result<List<Routine>> = withContext(Dispatchers.IO) {
        val result = tryEachBaseUrl { baseUrl ->
            val json = get(baseUrl, "/api/homecontrol/routines")
            val routinesArray = json.getJSONArray("routines")
            List(routinesArray.length()) { i ->
                parseRoutine(routinesArray.getJSONObject(i))
            }
        }
        result?.let { Result.success(it) } ?: Result.failure(Exception("Couldn't reach the server."))
    }

    suspend fun createRoutine(routine: Routine): Result<Routine> = withContext(Dispatchers.IO) {
        val result = tryEachBaseUrl { baseUrl ->
            val body = serializeRoutine(routine)
            val json = post(baseUrl, "/api/homecontrol/routines", body)
            parseRoutine(json.getJSONObject("routine"))
        }
        result?.let { Result.success(it) } ?: Result.failure(Exception("Couldn't reach the server."))
    }

    suspend fun updateRoutine(routine: Routine): Result<Routine> = withContext(Dispatchers.IO) {
        val result = tryEachBaseUrl { baseUrl ->
            val body = serializeRoutine(routine)
            val json = put(baseUrl, "/api/homecontrol/routines/${routine.id}", body)
            parseRoutine(json.getJSONObject("routine"))
        }
        result?.let { Result.success(it) } ?: Result.failure(Exception("Couldn't reach the server."))
    }

    suspend fun deleteRoutine(routineId: String): Result<Boolean> = withContext(Dispatchers.IO) {
        val result = tryEachBaseUrl { baseUrl ->
            delete(baseUrl, "/api/homecontrol/routines/$routineId")
        }
        result?.let { Result.success(it) } ?: Result.failure(Exception("Couldn't reach the server."))
    }

    suspend fun executeRoutine(routineId: String): Result<RoutineExecutionResponse> = withContext(Dispatchers.IO) {
        val result = tryEachBaseUrl { baseUrl ->
            val json = post(baseUrl, "/api/homecontrol/routines/$routineId/execute")
            parseRoutineExecutionResponse(json)
        }
        result?.let { Result.success(it) } ?: Result.failure(Exception("Couldn't reach the server."))
    }

    // ==================== Favorites ====================

    suspend fun getFavorites(): Result<List<String>> = withContext(Dispatchers.IO) {
        val result = tryEachBaseUrl { baseUrl ->
            val json = get(baseUrl, "/api/homecontrol/favorites")
            val favoritesArray = json.getJSONArray("favorites")
            List(favoritesArray.length()) { i ->
                favoritesArray.getString(i)
            }
        }
        result?.let { Result.success(it) } ?: Result.failure(Exception("Couldn't reach the server."))
    }

    suspend fun updateFavorites(deviceIds: List<String>): Result<List<String>> = withContext(Dispatchers.IO) {
        val result = tryEachBaseUrl { baseUrl ->
            val body = JSONObject().apply {
                put("deviceIds", JSONArray(deviceIds))
            }
            val json = put(baseUrl, "/api/homecontrol/favorites", body)
            val favoritesArray = json.getJSONArray("favorites")
            List(favoritesArray.length()) { i ->
                favoritesArray.getString(i)
            }
        }
        result?.let { Result.success(it) } ?: Result.failure(Exception("Couldn't reach the server."))
    }

    // ==================== Parsing ====================

    private fun parseDeviceState(json: JSONObject): DeviceState {
        val state = mutableMapOf<String, Any?>()
        json.keys().forEach { key ->
            state[key] = json.get(key)
        }
        return state
    }

    private fun parseDevice(json: JSONObject): Device {
        val capabilitiesArray = json.getJSONArray("capabilities")
        val stateJson = json.getJSONObject("state")

        return Device(
            id = json.getString("id"),
            name = json.getString("name"),
            roomId = json.optString("roomId").takeIf { it.isNotBlank() },
            type = DeviceType.fromString(json.getString("type")),
            online = json.getBoolean("online"),
            manufacturer = json.optString("manufacturer").takeIf { it.isNotBlank() },
            model = json.optString("model").takeIf { it.isNotBlank() },
            capabilities = List(capabilitiesArray.length()) { i ->
                Capability.fromString(capabilitiesArray.getString(i))
            },
            state = parseDeviceState(stateJson),
            isFavorite = json.getBoolean("isFavorite"),
            lastSeen = json.optLong("lastSeen").takeIf { it > 0 },
            createdAt = json.getLong("createdAt"),
            updatedAt = json.getLong("updatedAt")
        )
    }

    private fun parseRoom(json: JSONObject): Room {
        val deviceIdsArray = json.getJSONArray("deviceIds")

        return Room(
            id = json.getString("id"),
            name = json.getString("name"),
            deviceIds = List(deviceIdsArray.length()) { i ->
                deviceIdsArray.getString(i)
            },
            icon = json.optString("icon").takeIf { it.isNotBlank() },
            order = json.getInt("order"),
            createdAt = json.getLong("createdAt"),
            updatedAt = json.getLong("updatedAt")
        )
    }

    private fun parseRoutineTrigger(json: JSONObject): RoutineTrigger {
        val daysArray = json.optJSONArray("days")
        val days = daysArray?.let { List(it.length()) { i -> it.getInt(i) } }

        return RoutineTrigger(
            type = TriggerType.fromString(json.getString("type")),
            time = json.optString("time").takeIf { it.isNotBlank() },
            days = days,
            deviceId = json.optString("deviceId").takeIf { it.isNotBlank() },
            capability = json.optString("capability").takeIf { it.isNotBlank() }?.let { Capability.fromString(it) },
            operator = json.optString("operator").takeIf { it.isNotBlank() }?.let { ConditionOperator.fromSymbol(it) },
            value = if (json.has("value")) json.get("value") else null,
            offset = json.optInt("offset").takeIf { json.has("offset") }
        )
    }

    private fun parseRoutineCondition(json: JSONObject): RoutineCondition {
        return RoutineCondition(
            deviceId = json.getString("deviceId"),
            capability = Capability.fromString(json.getString("capability")),
            operator = ConditionOperator.fromSymbol(json.getString("operator")),
            value = json.get("value")
        )
    }

    private fun parseRoutineAction(json: JSONObject): RoutineAction {
        return RoutineAction(
            deviceId = json.getString("deviceId"),
            capability = Capability.fromString(json.getString("capability")),
            value = json.get("value"),
            delay = json.optInt("delay").takeIf { json.has("delay") }
        )
    }

    private fun parseRoutine(json: JSONObject): Routine {
        val triggersArray = json.getJSONArray("triggers")
        val conditionsArray = json.optJSONArray("conditions")
        val actionsArray = json.getJSONArray("actions")

        return Routine(
            id = json.getString("id"),
            name = json.getString("name"),
            description = json.optString("description").takeIf { it.isNotBlank() },
            enabled = json.getBoolean("enabled"),
            triggers = List(triggersArray.length()) { i ->
                parseRoutineTrigger(triggersArray.getJSONObject(i))
            },
            conditions = conditionsArray?.let { arr ->
                List(arr.length()) { i -> parseRoutineCondition(arr.getJSONObject(i)) }
            },
            logicalOperator = json.optString("logicalOperator").takeIf { it.isNotBlank() }?.let {
                LogicalOperator.fromString(it)
            } ?: LogicalOperator.AND,
            actions = List(actionsArray.length()) { i ->
                parseRoutineAction(actionsArray.getJSONObject(i))
            },
            lastRunAt = json.optLong("lastRunAt").takeIf { it > 0 },
            runCount = json.optInt("runCount").takeIf { json.has("runCount") },
            createdAt = json.getLong("createdAt"),
            updatedAt = json.getLong("updatedAt")
        )
    }

    private fun parseDeviceDiscoveryResponse(json: JSONObject): DeviceDiscoveryResponse {
        val devicesArray = json.getJSONArray("devices")

        return DeviceDiscoveryResponse(
            devicesFound = json.getInt("devicesFound"),
            newDevices = json.getInt("newDevices"),
            updatedDevices = json.getInt("updatedDevices"),
            devices = List(devicesArray.length()) { i ->
                parseDevice(devicesArray.getJSONObject(i))
            }
        )
    }

    private fun parseRoutineExecutionResponse(json: JSONObject): RoutineExecutionResponse {
        val errorsArray = json.optJSONArray("errors")
        val errors = errorsArray?.let { List(it.length()) { i -> it.getString(i) } }

        return RoutineExecutionResponse(
            success = json.getBoolean("success"),
            executedAt = json.getLong("executedAt"),
            actionsExecuted = json.getInt("actionsExecuted"),
            errors = errors
        )
    }

    // ==================== Serialization ====================

    private fun serializeDeviceState(state: DeviceState): JSONObject {
        return JSONObject().apply {
            state.forEach { (key, value) ->
                put(key, value)
            }
        }
    }

    private fun serializeRoom(room: Room): JSONObject {
        return JSONObject().apply {
            put("id", room.id)
            put("name", room.name)
            put("deviceIds", JSONArray(room.deviceIds))
            room.icon?.let { put("icon", it) }
            put("order", room.order)
        }
    }

    private fun serializeRoutineTrigger(trigger: RoutineTrigger): JSONObject {
        return JSONObject().apply {
            put("type", trigger.type.toApiString())
            trigger.time?.let { put("time", it) }
            trigger.days?.let { put("days", JSONArray(it)) }
            trigger.deviceId?.let { put("deviceId", it) }
            trigger.capability?.let { put("capability", it.toApiString()) }
            trigger.operator?.let { put("operator", it.symbol) }
            trigger.value?.let { put("value", it) }
            trigger.offset?.let { put("offset", it) }
        }
    }

    private fun serializeRoutineCondition(condition: RoutineCondition): JSONObject {
        return JSONObject().apply {
            put("deviceId", condition.deviceId)
            put("capability", condition.capability.toApiString())
            put("operator", condition.operator.symbol)
            put("value", condition.value)
        }
    }

    private fun serializeRoutineAction(action: RoutineAction): JSONObject {
        return JSONObject().apply {
            put("deviceId", action.deviceId)
            put("capability", action.capability.toApiString())
            put("value", action.value)
            action.delay?.let { put("delay", it) }
        }
    }

    private fun serializeRoutine(routine: Routine): JSONObject {
        return JSONObject().apply {
            put("id", routine.id)
            put("name", routine.name)
            routine.description?.let { put("description", it) }
            put("enabled", routine.enabled)
            put("triggers", JSONArray().apply {
                routine.triggers.forEach { put(serializeRoutineTrigger(it)) }
            })
            routine.conditions?.let { conditions ->
                put("conditions", JSONArray().apply {
                    conditions.forEach { put(serializeRoutineCondition(it)) }
                })
            }
            put("logicalOperator", routine.logicalOperator?.toApiString() ?: "AND")
            put("actions", JSONArray().apply {
                routine.actions.forEach { put(serializeRoutineAction(it)) }
            })
        }
    }
}
