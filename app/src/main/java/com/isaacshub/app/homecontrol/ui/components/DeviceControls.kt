package com.isaacshub.app.homecontrol.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.isaacshub.app.homecontrol.data.*

/**
 * Capability-specific controls for devices
 */

@Composable
fun PowerControl(
    device: Device,
    onCommandSent: (String, Any) -> Unit
) {
    val power = device.state.getPower() ?: false

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Power, "Power")
                Text(
                    text = "Power",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Switch(
                checked = power,
                onCheckedChange = { onCommandSent("power", it) },
                enabled = device.online
            )
        }
    }
}

@Composable
fun BrightnessControl(
    device: Device,
    onCommandSent: (String, Any) -> Unit
) {
    var brightness by remember(device.state.getBrightness()) {
        mutableStateOf((device.state.getBrightness() ?: 100).toFloat())
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Brightness6, "Brightness")
                    Text(
                        text = "Brightness",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    text = "${brightness.toInt()}%",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Slider(
                value = brightness,
                onValueChange = { brightness = it },
                onValueChangeFinished = {
                    onCommandSent("brightness", brightness.toInt())
                },
                valueRange = 0f..100f,
                enabled = device.online
            )
        }
    }
}

@Composable
fun ColorTemperatureControl(
    device: Device,
    onCommandSent: (String, Any) -> Unit
) {
    var colorTemp by remember(device.state.getColorTemperature()) {
        mutableStateOf((device.state.getColorTemperature() ?: 4000).toFloat())
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.WbSunny, "Color Temperature")
                    Text(
                        text = "Color Temperature",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    text = "${colorTemp.toInt()}K",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Slider(
                value = colorTemp,
                onValueChange = { colorTemp = it },
                onValueChangeFinished = {
                    onCommandSent("colorTemperature", colorTemp.toInt())
                },
                valueRange = 2000f..6500f,
                enabled = device.online
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Warm",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Cool",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun TemperatureControl(
    device: Device,
    onCommandSent: (String, Any) -> Unit
) {
    var temperature by remember(device.state.getTemperatureSetpoint()) {
        mutableStateOf(device.state.getTemperatureSetpoint() ?: 21.0)
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Thermostat, "Temperature")
                Text(
                    text = "Target Temperature",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        temperature = (temperature - 0.5).coerceAtLeast(10.0)
                        onCommandSent("temperatureSetpoint", temperature)
                    },
                    enabled = device.online
                ) {
                    Icon(Icons.Default.Remove, "Decrease")
                }

                Text(
                    text = "${"%.1f".format(temperature)}°C",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = {
                        temperature = (temperature + 0.5).coerceAtMost(30.0)
                        onCommandSent("temperatureSetpoint", temperature)
                    },
                    enabled = device.online
                ) {
                    Icon(Icons.Default.Add, "Increase")
                }
            }

            device.state.getTemperatureMeasurement()?.let { current ->
                Text(
                    text = "Current: ${"%.1f".format(current)}°C",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun LockControl(
    device: Device,
    onCommandSent: (String, Any) -> Unit
) {
    val locked = device.state.getLocked() ?: true
    var showUnlockConfirmation by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    if (locked) Icons.Default.Lock else Icons.Default.LockOpen,
                    "Lock Status"
                )
                Column {
                    Text(
                        text = "Lock",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = if (locked) "Locked" else "Unlocked",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Button(
                onClick = {
                    if (locked) {
                        // Lock without confirmation
                        onCommandSent("lock", true)
                    } else {
                        // Unlock requires confirmation
                        showUnlockConfirmation = true
                    }
                },
                enabled = device.online
            ) {
                Text(if (locked) "Unlock" else "Lock")
            }
        }
    }

    // Unlock confirmation dialog
    if (showUnlockConfirmation) {
        AlertDialog(
            onDismissRequest = { showUnlockConfirmation = false },
            title = { Text("Unlock Device") },
            text = { Text("Are you sure you want to unlock ${device.name}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onCommandSent("lock", false)
                        showUnlockConfirmation = false
                    }
                ) {
                    Text("Unlock")
                }
            },
            dismissButton = {
                TextButton(onClick = { showUnlockConfirmation = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SensorDisplay(
    device: Device
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Sensor Readings",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            device.state.getTemperatureMeasurement()?.let { temp ->
                SensorReading(
                    icon = Icons.Default.Thermostat,
                    label = "Temperature",
                    value = "${"%.1f".format(temp)}°C"
                )
            }

            device.state.getHumidity()?.let { humidity ->
                SensorReading(
                    icon = Icons.Default.WaterDrop,
                    label = "Humidity",
                    value = "$humidity%"
                )
            }

            device.state.getContact()?.let { contact ->
                SensorReading(
                    icon = Icons.Default.Sensor,
                    label = "Contact",
                    value = if (contact) "Open" else "Closed"
                )
            }

            device.state.getMotion()?.let { motion ->
                SensorReading(
                    icon = Icons.Default.Sensors,
                    label = "Motion",
                    value = if (motion) "Detected" else "Clear"
                )
            }

            device.state.getOccupancy()?.let { occupancy ->
                SensorReading(
                    icon = Icons.Default.Person,
                    label = "Occupancy",
                    value = if (occupancy) "Occupied" else "Empty"
                )
            }

            device.state.getBattery()?.let { battery ->
                SensorReading(
                    icon = Icons.Default.BatteryFull,
                    label = "Battery",
                    value = "$battery%"
                )
            }
        }
    }
}

@Composable
private fun SensorReading(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}
