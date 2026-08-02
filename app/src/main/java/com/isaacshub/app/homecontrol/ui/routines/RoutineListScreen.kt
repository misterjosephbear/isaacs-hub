package com.isaacshub.app.homecontrol.ui.routines

import androidx.compose.foundation.clickable
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
import com.isaacshub.app.homecontrol.ui.home.HomeControlViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineListScreen(
    onRoutineClick: (String) -> Unit,
    onCreateRoutine: () -> Unit,
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

    var showFilterMenu by remember { mutableStateOf(false) }
    var filterEnabled by remember { mutableStateOf<Boolean?>(null) }
    var filterTriggerType by remember { mutableStateOf<TriggerType?>(null) }

    val filteredRoutines = remember(uiState.routines, filterEnabled, filterTriggerType) {
        uiState.routines.filter { routine ->
            val matchesEnabled = filterEnabled == null || routine.enabled == filterEnabled
            val matchesTrigger = filterTriggerType == null ||
                routine.triggers.any { it.type == filterTriggerType }

            matchesEnabled && matchesTrigger
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Routines") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showFilterMenu = true }) {
                        Icon(Icons.Default.FilterList, "Filter")
                    }
                    IconButton(onClick = { viewModel?.refresh() }) {
                        Icon(Icons.Default.Refresh, "Refresh")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateRoutine) {
                Icon(Icons.Default.Add, "Create Routine")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Active filters
            if (filterEnabled != null || filterTriggerType != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filterEnabled?.let { enabled ->
                        FilterChip(
                            selected = true,
                            onClick = { filterEnabled = null },
                            label = { Text(if (enabled) "Enabled" else "Disabled") },
                            trailingIcon = { Icon(Icons.Default.Close, null) }
                        )
                    }
                    filterTriggerType?.let { trigger ->
                        FilterChip(
                            selected = true,
                            onClick = { filterTriggerType = null },
                            label = { Text(trigger.name) },
                            trailingIcon = { Icon(Icons.Default.Close, null) }
                        )
                    }
                }
            }

            // Routine list
            if (uiState.isLoading && uiState.routines.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (filteredRoutines.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = if (uiState.routines.isEmpty()) {
                                "No routines configured"
                            } else {
                                "No routines match the current filters"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (uiState.routines.isEmpty()) {
                            Button(onClick = onCreateRoutine) {
                                Text("Create Routine")
                            }
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = "${filteredRoutines.size} ${if (filteredRoutines.size == 1) "routine" else "routines"}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    items(filteredRoutines, key = { it.id }) { routine ->
                        RoutineCard(
                            routine = routine,
                            onClick = { onRoutineClick(routine.id) },
                            onToggleEnabled = { viewModel?.toggleRoutineEnabled(routine) },
                            onExecute = { viewModel?.executeRoutine(routine.id) }
                        )
                    }
                }
            }

            if (uiState.isLoading && uiState.routines.isNotEmpty()) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    // Filter menu
    DropdownMenu(
        expanded = showFilterMenu,
        onDismissRequest = { showFilterMenu = false }
    ) {
        DropdownMenuItem(
            text = { Text("All Routines") },
            onClick = {
                filterEnabled = null
                filterTriggerType = null
                showFilterMenu = false
            }
        )
        Divider()
        DropdownMenuItem(
            text = { Text("Enabled Only") },
            onClick = {
                filterEnabled = true
                showFilterMenu = false
            }
        )
        DropdownMenuItem(
            text = { Text("Disabled Only") },
            onClick = {
                filterEnabled = false
                showFilterMenu = false
            }
        )
        Divider()
        DropdownMenuItem(
            text = { Text("Manual Trigger") },
            onClick = {
                filterTriggerType = TriggerType.MANUAL
                showFilterMenu = false
            }
        )
        DropdownMenuItem(
            text = { Text("Schedule Trigger") },
            onClick = {
                filterTriggerType = TriggerType.SCHEDULE
                showFilterMenu = false
            }
        )
        DropdownMenuItem(
            text = { Text("Device State Trigger") },
            onClick = {
                filterTriggerType = TriggerType.DEVICE_STATE
                showFilterMenu = false
            }
        )
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

            Spacer(modifier = Modifier.height(12.dp))

            // Trigger info
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    getTriggerIcon(routine.triggers.firstOrNull()?.type),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = getTriggerDescription(routine.triggers),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

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

            routine.runCount?.let { count ->
                if (count > 0) {
                    Text(
                        text = "Run $count ${if (count == 1) "time" else "times"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun getTriggerIcon(triggerType: TriggerType?): androidx.compose.ui.graphics.vector.ImageVector {
    return when (triggerType) {
        TriggerType.MANUAL -> Icons.Default.TouchApp
        TriggerType.SCHEDULE -> Icons.Default.Schedule
        TriggerType.DEVICE_STATE -> Icons.Default.Sensors
        TriggerType.SUNRISE -> Icons.Default.WbSunny
        TriggerType.SUNSET -> Icons.Default.NightsStay
        null -> Icons.Default.HelpOutline
    }
}

@Composable
private fun getTriggerDescription(triggers: List<RoutineTrigger>): String {
    if (triggers.isEmpty()) return "No triggers"

    val trigger = triggers.first()
    return when (trigger.type) {
        TriggerType.MANUAL -> "Manual trigger"
        TriggerType.SCHEDULE -> {
            val time = trigger.time ?: "Unknown time"
            val days = trigger.days
            if (days.isNullOrEmpty()) {
                "Daily at $time"
            } else {
                "Scheduled: $time"
            }
        }
        TriggerType.DEVICE_STATE -> "When device state changes"
        TriggerType.SUNRISE -> {
            val offset = trigger.offset ?: 0
            if (offset == 0) "At sunrise"
            else if (offset > 0) "$offset min after sunrise"
            else "${-offset} min before sunrise"
        }
        TriggerType.SUNSET -> {
            val offset = trigger.offset ?: 0
            if (offset == 0) "At sunset"
            else if (offset > 0) "$offset min after sunset"
            else "${-offset} min before sunset"
        }
    }
}
