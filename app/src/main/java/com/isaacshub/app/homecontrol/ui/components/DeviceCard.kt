package com.isaacshub.app.homecontrol.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.isaacshub.app.homecontrol.data.*

@Composable
fun DeviceCard(
    device: Device,
    onClick: () -> Unit,
    onTogglePower: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Device icon
            Icon(
                imageVector = getDeviceIcon(device.type),
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = if (device.online) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Device info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = device.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (device.isFavorite) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Favorite",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Text(
                    text = getDeviceDescription(device),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (device.online) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )
            }

            // Primary control (power switch if available)
            if (device.capabilities.contains(Capability.POWER) && onTogglePower != null) {
                Switch(
                    checked = device.state.getPower() ?: false,
                    onCheckedChange = { onTogglePower() },
                    enabled = device.online
                )
            }
        }
    }
}

@Composable
private fun getDeviceIcon(type: DeviceType): androidx.compose.ui.graphics.vector.ImageVector {
    return when (type) {
        DeviceType.LIGHT -> Icons.Default.Lightbulb
        DeviceType.SWITCH -> Icons.Default.PowerSettingsNew
        DeviceType.PLUG -> Icons.Default.Power
        DeviceType.THERMOSTAT -> Icons.Default.Thermostat
        DeviceType.SENSOR -> Icons.Default.Sensors
        DeviceType.LOCK -> Icons.Default.Lock
        DeviceType.SCENE -> Icons.Default.Palette
        DeviceType.UNKNOWN -> Icons.Default.DeviceUnknown
    }
}

@Composable
private fun getDeviceDescription(device: Device): String {
    if (!device.online) {
        return "Offline"
    }

    val state = device.state

    return when (device.type) {
        DeviceType.LIGHT -> {
            if (state.getPower() == false) "Off"
            else {
                val brightness = state.getBrightness()
                if (brightness != null) "On - $brightness%" else "On"
            }
        }
        DeviceType.THERMOSTAT -> {
            val current = state.getTemperatureMeasurement()
            val target = state.getTemperatureSetpoint()
            when {
                current != null && target != null -> "$current°C → $target°C"
                target != null -> "Set to $target°C"
                else -> "Active"
            }
        }
        DeviceType.LOCK -> {
            if (state.getLocked() == true) "Locked" else "Unlocked"
        }
        DeviceType.SENSOR -> {
            val contact = state.getContact()
            val motion = state.getMotion()
            val occupancy = state.getOccupancy()
            when {
                contact != null -> if (contact) "Open" else "Closed"
                motion != null -> if (motion) "Motion detected" else "No motion"
                occupancy != null -> if (occupancy) "Occupied" else "Empty"
                else -> "Active"
            }
        }
        DeviceType.SWITCH, DeviceType.PLUG -> {
            if (state.getPower() == true) "On" else "Off"
        }
        else -> "Ready"
    }
}
