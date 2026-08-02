package com.isaacshub.app.homecontrol.ui.devices

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.isaacshub.app.App
import com.isaacshub.app.homecontrol.data.*
import com.isaacshub.app.homecontrol.ui.components.*
import com.isaacshub.app.homecontrol.ui.home.HomeControlViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceDetailScreen(
    deviceId: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val app = context.applicationContext as App

    val vaultConnection by app.vaultPreferencesRepository.connection.collectAsState(initial = null)

    val repository = remember(vaultConnection) {
        vaultConnection?.let {
            HomeControlRepository(HomeControlApiClient(it))
        }
    }

    val viewModel: HomeControlViewModel? = repository?.let { repo ->
        viewModel(factory = HomeControlViewModel.Factory(repo))
    }

    val uiState by (viewModel?.uiState?.collectAsState() ?: remember { mutableStateOf(com.isaacshub.app.homecontrol.ui.home.HomeControlUiState()) })

    val device = remember(uiState.devices) {
        uiState.devices.find { it.id == deviceId }
    }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showRoomDialog by remember { mutableStateOf(false) }

    device?.let { dev ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(dev.name) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { viewModel?.toggleFavorite(deviceId) }) {
                            Icon(
                                if (dev.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                                "Toggle Favorite",
                                tint = if (dev.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, "Delete Device")
                        }
                    }
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                // Device info card
                item {
                    DeviceInfoCard(device = dev, rooms = uiState.rooms, onChangeRoom = { showRoomDialog = true })
                }

                // Controls based on capabilities
                if (dev.capabilities.contains(Capability.POWER)) {
                    item {
                        PowerControl(
                            device = dev,
                            onCommandSent = { capability, value ->
                                viewModel?.sendDeviceCommand(deviceId, capability, value)
                            }
                        )
                    }
                }

                if (dev.capabilities.contains(Capability.BRIGHTNESS)) {
                    item {
                        BrightnessControl(
                            device = dev,
                            onCommandSent = { capability, value ->
                                viewModel?.sendDeviceCommand(deviceId, capability, value)
                            }
                        )
                    }
                }

                if (dev.capabilities.contains(Capability.COLOR_TEMPERATURE)) {
                    item {
                        ColorTemperatureControl(
                            device = dev,
                            onCommandSent = { capability, value ->
                                viewModel?.sendDeviceCommand(deviceId, capability, value)
                            }
                        )
                    }
                }

                if (dev.capabilities.contains(Capability.TEMPERATURE_SETPOINT)) {
                    item {
                        TemperatureControl(
                            device = dev,
                            onCommandSent = { capability, value ->
                                viewModel?.sendDeviceCommand(deviceId, capability, value)
                            }
                        )
                    }
                }

                if (dev.capabilities.contains(Capability.LOCK)) {
                    item {
                        LockControl(
                            device = dev,
                            onCommandSent = { capability, value ->
                                viewModel?.sendDeviceCommand(deviceId, capability, value)
                            }
                        )
                    }
                }

                // Sensor display for read-only capabilities
                val hasSensorCapabilities = dev.capabilities.any {
                    it in listOf(
                        Capability.TEMPERATURE_MEASUREMENT,
                        Capability.HUMIDITY,
                        Capability.CONTACT,
                        Capability.MOTION,
                        Capability.OCCUPANCY,
                        Capability.BATTERY
                    )
                }

                if (hasSensorCapabilities) {
                    item {
                        SensorDisplay(device = dev)
                    }
                }

                // Capabilities list
                item {
                    CapabilitiesCard(device = dev)
                }
            }
        }

        // Delete confirmation dialog
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Device") },
                text = { Text("Are you sure you want to delete ${dev.name}? This cannot be undone.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // TODO: Implement delete device
                            showDeleteDialog = false
                            onBack()
                        }
                    ) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Room assignment dialog
        if (showRoomDialog) {
            RoomSelectionDialog(
                currentRoomId = dev.roomId,
                rooms = uiState.rooms,
                onRoomSelected = { roomId ->
                    // TODO: Implement room assignment
                    showRoomDialog = false
                },
                onDismiss = { showRoomDialog = false }
            )
        }
    } ?: run {
        // Device not found
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Device not found",
                    style = MaterialTheme.typography.titleLarge
                )
                Button(onClick = onBack) {
                    Text("Go Back")
                }
            }
        }
    }
}

@Composable
private fun DeviceInfoCard(
    device: Device,
    rooms: List<Room>,
    onChangeRoom: () -> Unit
) {
    val dateFormatter = remember { SimpleDateFormat("MMM d, h:mm a", Locale.getDefault()) }
    val room = rooms.find { it.id == device.roomId }

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
                text = "Device Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            InfoRow("Type", device.type.name.lowercase().replaceFirstChar { it.uppercase() })
            InfoRow("Status", if (device.online) "Online" else "Offline")

            device.manufacturer?.let { manufacturer ->
                InfoRow("Manufacturer", manufacturer)
            }

            device.model?.let { model ->
                InfoRow("Model", model)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Room",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(onClick = onChangeRoom) {
                    Text(room?.name ?: "Unassigned")
                    Icon(Icons.Default.Edit, null, modifier = Modifier.size(16.dp))
                }
            }

            device.lastSeen?.let { lastSeen ->
                InfoRow("Last Seen", dateFormatter.format(Date(lastSeen)))
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun CapabilitiesCard(device: Device) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Capabilities",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            device.capabilities.forEach { capability ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = capability.toApiString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun RoomSelectionDialog(
    currentRoomId: String?,
    rooms: List<Room>,
    onRoomSelected: (String?) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Room") },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    TextButton(
                        onClick = { onRoomSelected(null) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Unassigned", modifier = Modifier.fillMaxWidth())
                    }
                }

                items(rooms.size) { index ->
                    val room = rooms[index]
                    TextButton(
                        onClick = { onRoomSelected(room.id) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(room.name)
                            if (room.id == currentRoomId) {
                                Icon(Icons.Default.Check, "Current room")
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
