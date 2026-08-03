package com.isaacshub.app.homecontrol.ui.devices

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.isaacshub.app.homecontrol.ui.components.DeviceCard
import com.isaacshub.app.homecontrol.ui.home.HomeControlViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceListScreen(
    onDeviceClick: (String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val app = context.applicationContext as App

    val vaultConnection by app.vaultPreferencesRepository.connection.collectAsState(initial = null)

    // Show error if no connection
    if (vaultConnection == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(32.dp)
            ) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.error
                )
                Text(
                    text = "No vault connection",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.error
                )
                Text(
                    text = "Please connect to your vault first",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Button(onClick = onBack) {
                    Text("Go Back")
                }
            }
        }
        return
    }

    val repository = remember(vaultConnection) {
        HomeControlRepository(HomeControlApiClient(vaultConnection!!))
    }

    val viewModel: HomeControlViewModel = viewModel(factory = HomeControlViewModel.Factory(repository))

    val uiState by viewModel.uiState.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var filterRoom by remember { mutableStateOf<String?>(null) }
    var filterType by remember { mutableStateOf<DeviceType?>(null) }
    var filterOnlineOnly by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }

    val filteredDevices = remember(uiState.devices, searchQuery, filterRoom, filterType, filterOnlineOnly) {
        uiState.devices.filter { device ->
            val matchesSearch = searchQuery.isEmpty() ||
                device.name.contains(searchQuery, ignoreCase = true)
            val matchesRoom = filterRoom == null || device.roomId == filterRoom
            val matchesType = filterType == null || device.type == filterType
            val matchesOnline = !filterOnlineOnly || device.online

            matchesSearch && matchesRoom && matchesType && matchesOnline
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Devices") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(Icons.Default.FilterList, "Filter")
                    }
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(Icons.Default.Refresh, "Refresh")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search devices...") },
                leadingIcon = { Icon(Icons.Default.Search, "Search") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, "Clear")
                        }
                    }
                },
                singleLine = true
            )

            // Active filters chips
            if (filterRoom != null || filterType != null || filterOnlineOnly) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filterRoom?.let { roomId ->
                        val room = uiState.rooms.find { it.id == roomId }
                        room?.let {
                            FilterChip(
                                selected = true,
                                onClick = { filterRoom = null },
                                label = { Text(it.name) },
                                trailingIcon = { Icon(Icons.Default.Close, null) }
                            )
                        }
                    }
                    filterType?.let { type ->
                        FilterChip(
                            selected = true,
                            onClick = { filterType = null },
                            label = { Text(type.name) },
                            trailingIcon = { Icon(Icons.Default.Close, null) }
                        )
                    }
                    if (filterOnlineOnly) {
                        FilterChip(
                            selected = true,
                            onClick = { filterOnlineOnly = false },
                            label = { Text("Online only") },
                            trailingIcon = { Icon(Icons.Default.Close, null) }
                        )
                    }
                }
            }

            // Device list
            if (uiState.isLoading && uiState.devices.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (filteredDevices.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (uiState.devices.isEmpty()) {
                            "No devices found. Tap discover to find devices."
                        } else {
                            "No devices match the current filters."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = "${filteredDevices.size} ${if (filteredDevices.size == 1) "device" else "devices"}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    items(filteredDevices, key = { it.id }) { device ->
                        DeviceCard(
                            device = device,
                            onClick = { onDeviceClick(device.id) },
                            onTogglePower = { viewModel.toggleDevicePower(device.id) }
                        )
                    }
                }
            }

            if (uiState.isLoading && uiState.devices.isNotEmpty()) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    // Filter dialog
    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title = { Text("Filter Devices") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Filter by room
                    Text("Room", style = MaterialTheme.typography.titleSmall)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = filterRoom == null,
                            onClick = { filterRoom = null },
                            label = { Text("All") }
                        )
                        uiState.rooms.take(3).forEach { room ->
                            FilterChip(
                                selected = filterRoom == room.id,
                                onClick = { filterRoom = room.id },
                                label = { Text(room.name) }
                            )
                        }
                    }

                    // Filter by type
                    Text("Type", style = MaterialTheme.typography.titleSmall)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = filterType == null,
                            onClick = { filterType = null },
                            label = { Text("All") }
                        )
                        listOf(DeviceType.LIGHT, DeviceType.SWITCH, DeviceType.SENSOR).forEach { type ->
                            FilterChip(
                                selected = filterType == type,
                                onClick = { filterType = type },
                                label = { Text(type.name) }
                            )
                        }
                    }

                    // Filter by online status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Online only", style = MaterialTheme.typography.titleSmall)
                        Switch(
                            checked = filterOnlineOnly,
                            onCheckedChange = { filterOnlineOnly = it }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFilterDialog = false }) {
                    Text("Done")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    filterRoom = null
                    filterType = null
                    filterOnlineOnly = false
                    showFilterDialog = false
                }) {
                    Text("Clear All")
                }
            }
        )
    }
}
