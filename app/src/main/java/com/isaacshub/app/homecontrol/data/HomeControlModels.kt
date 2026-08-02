package com.isaacshub.app.homecontrol.data

import kotlinx.serialization.Serializable

/**
 * Data models for the Home Control feature - Matter-enabled smart home device control.
 *
 * These models match the server-side TypeScript types from homecontrol/types.ts
 */

// ==================== Device Types & Capabilities ====================

enum class DeviceType {
    LIGHT,
    SWITCH,
    PLUG,
    THERMOSTAT,
    SENSOR,
    LOCK,
    SCENE,
    UNKNOWN;

    companion object {
        fun fromString(type: String): DeviceType = when (type.lowercase()) {
            "light" -> LIGHT
            "switch" -> SWITCH
            "plug" -> PLUG
            "thermostat" -> THERMOSTAT
            "sensor" -> SENSOR
            "lock" -> LOCK
            "scene" -> SCENE
            else -> UNKNOWN
        }
    }

    fun toApiString(): String = name.lowercase()
}

enum class Capability {
    POWER,
    BRIGHTNESS,
    COLOR_TEMPERATURE,
    COLOR,
    TEMPERATURE_SETPOINT,
    TEMPERATURE_MEASUREMENT,
    OCCUPANCY,
    CONTACT,
    LOCK,
    MOTION,
    HUMIDITY,
    BATTERY;

    companion object {
        fun fromString(capability: String): Capability = when (capability) {
            "power" -> POWER
            "brightness" -> BRIGHTNESS
            "colorTemperature" -> COLOR_TEMPERATURE
            "color" -> COLOR
            "temperatureSetpoint" -> TEMPERATURE_SETPOINT
            "temperatureMeasurement" -> TEMPERATURE_MEASUREMENT
            "occupancy" -> OCCUPANCY
            "contact" -> CONTACT
            "lock" -> LOCK
            "motion" -> MOTION
            "humidity" -> HUMIDITY
            "battery" -> BATTERY
            else -> throw IllegalArgumentException("Unknown capability: $capability")
        }
    }

    fun toApiString(): String = when (this) {
        POWER -> "power"
        BRIGHTNESS -> "brightness"
        COLOR_TEMPERATURE -> "colorTemperature"
        COLOR -> "color"
        TEMPERATURE_SETPOINT -> "temperatureSetpoint"
        TEMPERATURE_MEASUREMENT -> "temperatureMeasurement"
        OCCUPANCY -> "occupancy"
        CONTACT -> "contact"
        LOCK -> "lock"
        MOTION -> "motion"
        HUMIDITY -> "humidity"
        BATTERY -> "battery"
    }
}

// ==================== Device State ====================

@Serializable
data class ColorValue(
    val hue: Int, // 0-360
    val saturation: Int // 0-100
)

// Flexible state map - capability names as keys
typealias DeviceState = Map<String, Any?>

// Helper to get typed values from state
fun DeviceState.getPower(): Boolean? = this["power"] as? Boolean
fun DeviceState.getBrightness(): Int? = (this["brightness"] as? Number)?.toInt()
fun DeviceState.getColorTemperature(): Int? = (this["colorTemperature"] as? Number)?.toInt()
fun DeviceState.getTemperatureSetpoint(): Double? = (this["temperatureSetpoint"] as? Number)?.toDouble()
fun DeviceState.getTemperatureMeasurement(): Double? = (this["temperatureMeasurement"] as? Number)?.toDouble()
fun DeviceState.getLocked(): Boolean? = (this["locked"] as? Boolean) ?: (this["lock"] as? Boolean)
fun DeviceState.getContact(): Boolean? = this["contact"] as? Boolean
fun DeviceState.getOccupancy(): Boolean? = this["occupancy"] as? Boolean
fun DeviceState.getMotion(): Boolean? = this["motion"] as? Boolean
fun DeviceState.getHumidity(): Int? = (this["humidity"] as? Number)?.toInt()
fun DeviceState.getBattery(): Int? = (this["battery"] as? Number)?.toInt()

// ==================== Device Model ====================

data class Device(
    val id: String,
    val name: String,
    val roomId: String? = null,
    val type: DeviceType,
    val online: Boolean,
    val manufacturer: String? = null,
    val model: String? = null,
    val capabilities: List<Capability>,
    val state: DeviceState,
    val isFavorite: Boolean,
    val lastSeen: Long? = null,
    val createdAt: Long,
    val updatedAt: Long
)

// ==================== Room Model ====================

data class Room(
    val id: String,
    val name: String,
    val deviceIds: List<String>,
    val icon: String? = null,
    val order: Int,
    val createdAt: Long,
    val updatedAt: Long
)

// ==================== Routine Models ====================

enum class TriggerType {
    MANUAL,
    SCHEDULE,
    DEVICE_STATE,
    SUNRISE,
    SUNSET;

    companion object {
        fun fromString(type: String): TriggerType = when (type.lowercase()) {
            "manual" -> MANUAL
            "schedule" -> SCHEDULE
            "device_state" -> DEVICE_STATE
            "sunrise" -> SUNRISE
            "sunset" -> SUNSET
            else -> MANUAL
        }
    }

    fun toApiString(): String = when (this) {
        MANUAL -> "manual"
        SCHEDULE -> "schedule"
        DEVICE_STATE -> "device_state"
        SUNRISE -> "sunrise"
        SUNSET -> "sunset"
    }
}

enum class ConditionOperator(val symbol: String) {
    EQUALS("="),
    NOT_EQUALS("!="),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    GREATER_THAN_OR_EQUAL(">="),
    LESS_THAN_OR_EQUAL("<=");

    companion object {
        fun fromSymbol(symbol: String): ConditionOperator = values().first { it.symbol == symbol }
    }
}

data class RoutineTrigger(
    val type: TriggerType,
    val time: String? = null, // HH:mm format
    val days: List<Int>? = null, // 0=Sunday, 1=Monday, etc.
    val deviceId: String? = null,
    val capability: Capability? = null,
    val operator: ConditionOperator? = null,
    val value: Any? = null,
    val offset: Int? = null // minutes offset for sunrise/sunset
)

data class RoutineCondition(
    val deviceId: String,
    val capability: Capability,
    val operator: ConditionOperator,
    val value: Any
)

data class RoutineAction(
    val deviceId: String,
    val capability: Capability,
    val value: Any,
    val delay: Int? = null // milliseconds
)

enum class LogicalOperator {
    AND,
    OR;

    fun toApiString(): String = name
    companion object {
        fun fromString(value: String): LogicalOperator = valueOf(value.uppercase())
    }
}

data class Routine(
    val id: String,
    val name: String,
    val description: String? = null,
    val enabled: Boolean,
    val triggers: List<RoutineTrigger>,
    val conditions: List<RoutineCondition>? = null,
    val logicalOperator: LogicalOperator? = LogicalOperator.AND,
    val actions: List<RoutineAction>,
    val lastRunAt: Long? = null,
    val runCount: Int? = null,
    val createdAt: Long,
    val updatedAt: Long
)

// ==================== API Request/Response Types ====================

data class DeviceCommandRequest(
    val capability: String,
    val value: Any
)

data class DeviceDiscoveryResponse(
    val devicesFound: Int,
    val newDevices: Int,
    val updatedDevices: Int,
    val devices: List<Device>
)

data class RoutineExecutionResponse(
    val success: Boolean,
    val executedAt: Long,
    val actionsExecuted: Int,
    val errors: List<String>? = null
)
