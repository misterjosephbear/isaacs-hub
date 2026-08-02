package com.isaacshub.app.homecontrol.ui.routines

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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineBuilderScreen(
    routineId: String?,
    onSaved: () -> Unit,
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

    val existingRoutine = remember(routineId, uiState.routines) {
        routineId?.let { id -> uiState.routines.find { it.id == id } }
    }

    var name by remember(existingRoutine) { mutableStateOf(existingRoutine?.name ?: "") }
    var description by remember(existingRoutine) { mutableStateOf(existingRoutine?.description ?: "") }
    var enabled by remember(existingRoutine) { mutableStateOf(existingRoutine?.enabled ?: true) }
    var triggers by remember(existingRoutine) { mutableStateOf(existingRoutine?.triggers ?: listOf(RoutineTrigger(TriggerType.MANUAL))) }
    var conditions by remember(existingRoutine) { mutableStateOf(existingRoutine?.conditions ?: emptyList()) }
    var logicalOperator by remember(existingRoutine) { mutableStateOf(existingRoutine?.logicalOperator ?: LogicalOperator.AND) }
    var actions by remember(existingRoutine) { mutableStateOf(existingRoutine?.actions ?: emptyList()) }

    var showAddTriggerDialog by remember { mutableStateOf(false) }
    var showAddConditionDialog by remember { mutableStateOf(false) }
    var showAddActionDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val isValid = name.isNotBlank() && triggers.isNotEmpty() && actions.isNotEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (existingRoutine != null) "Edit Routine" else "Create Routine") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            scope.launch {
                                val routine = Routine(
                                    id = existingRoutine?.id ?: "",
                                    name = name,
                                    description = description.ifBlank { null },
                                    enabled = enabled,
                                    triggers = triggers,
                                    conditions = conditions.ifEmpty { null },
                                    logicalOperator = if (conditions.isNotEmpty()) logicalOperator else null,
                                    actions = actions,
                                    lastRunAt = existingRoutine?.lastRunAt,
                                    runCount = existingRoutine?.runCount,
                                    createdAt = existingRoutine?.createdAt ?: System.currentTimeMillis(),
                                    updatedAt = System.currentTimeMillis()
                                )

                                val result = if (existingRoutine != null) {
                                    repository?.updateRoutine(routine)
                                } else {
                                    repository?.createRoutine(routine)
                                }

                                result?.fold(
                                    onSuccess = { onSaved() },
                                    onFailure = { error ->
                                        snackbarHostState.showSnackbar(
                                            error.message ?: "Failed to save routine"
                                        )
                                    }
                                )
                            }
                        },
                        enabled = isValid
                    ) {
                        Text("Save")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Basic info
            item {
                Card {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Basic Information",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description (optional)") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 3
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Enabled", style = MaterialTheme.typography.bodyMedium)
                            Switch(
                                checked = enabled,
                                onCheckedChange = { enabled = it }
                            )
                        }
                    }
                }
            }

            // Triggers section
            item {
                SectionHeader(
                    title = "Triggers",
                    icon = Icons.Default.PlayArrow,
                    onAdd = { showAddTriggerDialog = true }
                )
            }

            if (triggers.isEmpty()) {
                item {
                    Text(
                        text = "Add at least one trigger",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(triggers) { trigger ->
                    TriggerCard(
                        trigger = trigger,
                        onRemove = { triggers = triggers - trigger }
                    )
                }
            }

            // Conditions section (optional)
            item {
                SectionHeader(
                    title = "Conditions (optional)",
                    icon = Icons.Default.Rule,
                    onAdd = { showAddConditionDialog = true }
                )
            }

            if (conditions.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Match:", style = MaterialTheme.typography.bodyMedium)
                        FilterChip(
                            selected = logicalOperator == LogicalOperator.AND,
                            onClick = { logicalOperator = LogicalOperator.AND },
                            label = { Text("All") }
                        )
                        FilterChip(
                            selected = logicalOperator == LogicalOperator.OR,
                            onClick = { logicalOperator = LogicalOperator.OR },
                            label = { Text("Any") }
                        )
                    }
                }

                items(conditions) { condition ->
                    ConditionCard(
                        condition = condition,
                        devices = uiState.devices,
                        onRemove = { conditions = conditions - condition }
                    )
                }
            }

            // Actions section
            item {
                SectionHeader(
                    title = "Actions",
                    icon = Icons.Default.FlashOn,
                    onAdd = { showAddActionDialog = true }
                )
            }

            if (actions.isEmpty()) {
                item {
                    Text(
                        text = "Add at least one action",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(actions) { action ->
                    ActionCard(
                        action = action,
                        devices = uiState.devices,
                        onRemove = { actions = actions - action }
                    )
                }
            }
        }
    }

    // Add trigger dialog
    if (showAddTriggerDialog) {
        AddTriggerDialog(
            onDismiss = { showAddTriggerDialog = false },
            onAdd = { trigger ->
                triggers = triggers + trigger
                showAddTriggerDialog = false
            }
        )
    }

    // Add condition dialog
    if (showAddConditionDialog) {
        AddConditionDialog(
            devices = uiState.devices,
            onDismiss = { showAddConditionDialog = false },
            onAdd = { condition ->
                conditions = conditions + condition
                showAddConditionDialog = false
            }
        )
    }

    // Add action dialog
    if (showAddActionDialog) {
        AddActionDialog(
            devices = uiState.devices,
            onDismiss = { showAddActionDialog = false },
            onAdd = { action ->
                actions = actions + action
                showAddActionDialog = false
            }
        )
    }
}

@Composable
private fun SectionHeader(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onAdd: () -> Unit
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
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        IconButton(onClick = onAdd) {
            Icon(Icons.Default.Add, "Add")
        }
    }
}

@Composable
private fun TriggerCard(
    trigger: RoutineTrigger,
    onRemove: () -> Unit
) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = trigger.type.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                trigger.time?.let {
                    Text("Time: $it", style = MaterialTheme.typography.bodySmall)
                }
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, "Remove")
            }
        }
    }
}

@Composable
private fun ConditionCard(
    condition: RoutineCondition,
    devices: List<Device>,
    onRemove: () -> Unit
) {
    val device = devices.find { it.id == condition.deviceId }

    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = device?.name ?: "Unknown device",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${condition.capability.toApiString()} ${condition.operator.symbol} ${condition.value}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, "Remove")
            }
        }
    }
}

@Composable
private fun ActionCard(
    action: RoutineAction,
    devices: List<Device>,
    onRemove: () -> Unit
) {
    val device = devices.find { it.id == action.deviceId }

    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = device?.name ?: "Unknown device",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${action.capability.toApiString()} → ${action.value}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                action.delay?.let { delay ->
                    Text(
                        text = "Delay: ${delay}ms",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, "Remove")
            }
        }
    }
}

@Composable
private fun AddTriggerDialog(
    onDismiss: () -> Unit,
    onAdd: (RoutineTrigger) -> Unit
) {
    var selectedType by remember { mutableStateOf(TriggerType.MANUAL) }
    var time by remember { mutableStateOf("12:00") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Trigger") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Trigger Type", style = MaterialTheme.typography.titleSmall)
                TriggerType.values().forEach { type ->
                    FilterChip(
                        selected = selectedType == type,
                        onClick = { selectedType = type },
                        label = { Text(type.name) }
                    )
                }

                if (selectedType == TriggerType.SCHEDULE) {
                    OutlinedTextField(
                        value = time,
                        onValueChange = { time = it },
                        label = { Text("Time (HH:mm)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val trigger = RoutineTrigger(
                        type = selectedType,
                        time = if (selectedType == TriggerType.SCHEDULE) time else null
                    )
                    onAdd(trigger)
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun AddConditionDialog(
    devices: List<Device>,
    onDismiss: () -> Unit,
    onAdd: (RoutineCondition) -> Unit
) {
    var selectedDevice by remember { mutableStateOf<Device?>(null) }
    var selectedCapability by remember { mutableStateOf<Capability?>(null) }
    var operator by remember { mutableStateOf(ConditionOperator.EQUALS) }
    var value by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Condition") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Device", style = MaterialTheme.typography.titleSmall)
                // Simplified: Just show first few devices
                devices.take(5).forEach { device ->
                    FilterChip(
                        selected = selectedDevice == device,
                        onClick = { selectedDevice = device },
                        label = { Text(device.name) }
                    )
                }

                selectedDevice?.let { device ->
                    Text("Capability", style = MaterialTheme.typography.titleSmall)
                    device.capabilities.take(3).forEach { capability ->
                        FilterChip(
                            selected = selectedCapability == capability,
                            onClick = { selectedCapability = capability },
                            label = { Text(capability.toApiString()) }
                        )
                    }
                }

                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = { Text("Value") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    selectedDevice?.let { device ->
                        selectedCapability?.let { capability ->
                            val condition = RoutineCondition(
                                deviceId = device.id,
                                capability = capability,
                                operator = operator,
                                value = value
                            )
                            onAdd(condition)
                        }
                    }
                },
                enabled = selectedDevice != null && selectedCapability != null && value.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun AddActionDialog(
    devices: List<Device>,
    onDismiss: () -> Unit,
    onAdd: (RoutineAction) -> Unit
) {
    var selectedDevice by remember { mutableStateOf<Device?>(null) }
    var selectedCapability by remember { mutableStateOf<Capability?>(null) }
    var value by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Action") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Device", style = MaterialTheme.typography.titleSmall)
                // Simplified: Just show first few devices
                devices.take(5).forEach { device ->
                    FilterChip(
                        selected = selectedDevice == device,
                        onClick = { selectedDevice = device },
                        label = { Text(device.name) }
                    )
                }

                selectedDevice?.let { device ->
                    Text("Capability", style = MaterialTheme.typography.titleSmall)
                    device.capabilities.filter { it in listOf(Capability.POWER, Capability.BRIGHTNESS, Capability.LOCK) }.forEach { capability ->
                        FilterChip(
                            selected = selectedCapability == capability,
                            onClick = { selectedCapability = capability },
                            label = { Text(capability.toApiString()) }
                        )
                    }
                }

                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = { Text("Value") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    selectedDevice?.let { device ->
                        selectedCapability?.let { capability ->
                            val action = RoutineAction(
                                deviceId = device.id,
                                capability = capability,
                                value = value
                            )
                            onAdd(action)
                        }
                    }
                },
                enabled = selectedDevice != null && selectedCapability != null && value.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
