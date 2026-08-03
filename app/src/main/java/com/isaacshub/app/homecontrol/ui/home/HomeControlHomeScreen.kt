package com.isaacshub.app.homecontrol.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeControlHomeScreen(
    onDeviceClick: (String) -> Unit,
    onRoutineClick: (String) -> Unit,
    onCreateRoutine: () -> Unit,
    onAddDevice: () -> Unit,
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
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

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Overview", "Devices", "Routines", "Rooms")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home Control") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onAddDevice) {
                        Icon(Icons.Default.Add, "Add Device")
                    }
                    IconButton(onClick = { viewModel.discoverDevices() }) {
                        Icon(Icons.Default.Search, "Discover Devices")
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
            // Error message
            uiState.error?.let { errorMsg ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = errorMsg,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { viewModel.clearError() }) {
                            Icon(Icons.Default.Close, "Dismiss", tint = MaterialTheme.colorScheme.onErrorContainer)
                        }
                    }
                }
            }

            // Tabs
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            // Tab content
            when (selectedTab) {
                0 -> OverviewTab(
                    uiState = uiState,
                    onDeviceClick = onDeviceClick,
                    onToggleDevice = { deviceId -> viewModel.toggleDevicePower(deviceId) },
                    onExecuteRoutine = { routineId -> viewModel.executeRoutine(routineId) }
                )
                1 -> DevicesTab(
                    uiState = uiState,
                    onDeviceClick = onDeviceClick,
                    onToggleDevice = { deviceId -> viewModel.toggleDevicePower(deviceId) }
                )
                2 -> RoutinesTab(
                    uiState = uiState,
                    onRoutineClick = onRoutineClick,
                    onCreateRoutine = onCreateRoutine,
                    onToggleRoutine = { routine -> viewModel.toggleRoutineEnabled(routine) },
                    onExecuteRoutine = { routineId -> viewModel.executeRoutine(routineId) }
                )
                3 -> RoomsTab(
                    uiState = uiState,
                    onDeviceClick = onDeviceClick,
                    onToggleDevice = { deviceId -> viewModel.toggleDevicePower(deviceId) }
                )
            }

            // Loading indicator
            if (uiState.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun OverviewTab(
    uiState: HomeControlUiState,
    onDeviceClick: (String) -> Unit,
    onToggleDevice: (String) -> Unit,
    onExecuteRoutine: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Favorites section
        if (uiState.favoriteDevices.isNotEmpty()) {
            item {
                SectionHeader(
                    title = "Favorites",
                    icon = Icons.Default.Star
                )
            }
            items(uiState.favoriteDevices, key = { it.id }) { device ->
                DeviceCard(
                    device = device,
                    onClick = { onDeviceClick(device.id) },
                    onTogglePower = { onToggleDevice(device.id) }
                )
            }
        }

        // Quick access routines
        val manualRoutines = uiState.routines.filter { routine ->
            routine.triggers.any { it.type == TriggerType.MANUAL }
        }.take(3)

        if (manualRoutines.isNotEmpty()) {
            item {
                SectionHeader(
                    title = "Quick Routines",
                    icon = Icons.Default.PlayArrow
                )
            }
            items(manualRoutines, key = { it.id }) { routine ->
                QuickRoutineCard(
                    routine = routine,
                    onExecute = { onExecuteRoutine(routine.id) }
                )
            }
        }

        // Rooms grid
        if (uiState.rooms.isNotEmpty()) {
            item {
                SectionHeader(
                    title = "Rooms",
                    icon = Icons.Default.Home
                )
            }
            item {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(((uiState.rooms.size + 1) / 2 * 100).dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.rooms, key = { it.id }) { room ->
                        RoomCard(
                            room = room,
                            deviceCount = uiState.devices.count { it.roomId == room.id }
                        )
                    }
                }
            }
        }

        // Status summary
        item {
            StatusSummaryCard(uiState)
        }
    }
}

@Composable
private fun DevicesTab(
    uiState: HomeControlUiState,
    onDeviceClick: (String) -> Unit,
    onToggleDevice: (String) -> Unit
) {
    if (uiState.devices.isEmpty() && !uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No devices found. Tap the search icon to discover devices.",
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
            items(uiState.devices, key = { it.id }) { device ->
                DeviceCard(
                    device = device,
                    onClick = { onDeviceClick(device.id) },
                    onTogglePower = { onToggleDevice(device.id) }
                )
            }
        }
    }
}

@Composable
private fun RoutinesTab(
    uiState: HomeControlUiState,
    onRoutineClick: (String) -> Unit,
    onCreateRoutine: () -> Unit,
    onToggleRoutine: (Routine) -> Unit,
    onExecuteRoutine: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (uiState.routines.isEmpty() && !uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No routines configured",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(uiState.routines, key = { it.id }) { routine ->
                    RoutineCard(
                        routine = routine,
                        onClick = { onRoutineClick(routine.id) },
                        onToggleEnabled = { onToggleRoutine(routine) },
                        onExecute = { onExecuteRoutine(routine.id) }
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = onCreateRoutine,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, "Create Routine")
        }
    }
}

@Composable
private fun RoomsTab(
    uiState: HomeControlUiState,
    onDeviceClick: (String) -> Unit,
    onToggleDevice: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        uiState.devicesGroupedByRoom.forEach { (room, devices) ->
            item {
                SectionHeader(
                    title = room?.name ?: "Unassigned",
                    icon = Icons.Default.Home
                )
            }
            items(devices, key = { it.id }) { device ->
                DeviceCard(
                    device = device,
                    onClick = { onDeviceClick(device.id) },
                    onTogglePower = { onToggleDevice(device.id) }
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
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
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun QuickRoutineCard(
    routine: Routine,
    onExecute: () -> Unit
) {
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = routine.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                routine.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(
                onClick = onExecute,
                enabled = routine.enabled
            ) {
                Icon(Icons.Default.PlayArrow, "Execute")
            }
        }
    }
}

@Composable
private fun RoomCard(
    room: Room,
    deviceCount: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = room.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "$deviceCount ${if (deviceCount == 1) "device" else "devices"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun RoutineCard(
    routine: Routine,
    onClick: () -> Unit,
    onToggleEnabled: () -> Unit,
    onExecute: () -> Unit
) {
    val dateFormatter = remember { SimpleDateFormat("MMM d, h:mm a", Locale.getDefault()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = routine.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    routine.description?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (routine.triggers.any { it.type == TriggerType.MANUAL }) {
                        IconButton(
                            onClick = onExecute,
                            enabled = routine.enabled
                        ) {
                            Icon(Icons.Default.PlayArrow, "Execute")
                        }
                    }
                    Switch(
                        checked = routine.enabled,
                        onCheckedChange = { onToggleEnabled() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${routine.actions.size} ${if (routine.actions.size == 1) "action" else "actions"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                routine.lastRunAt?.let { lastRun ->
                    Text(
                        text = "Last run: ${dateFormatter.format(Date(lastRun))}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusSummaryCard(uiState: HomeControlUiState) {
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
                text = "System Status",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total Devices:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${uiState.devices.size}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Online:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${uiState.onlineDevices.size}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Offline:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
                Text(
                    text = "${uiState.offlineDevices.size}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Active Routines:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${uiState.routines.count { it.enabled }}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
