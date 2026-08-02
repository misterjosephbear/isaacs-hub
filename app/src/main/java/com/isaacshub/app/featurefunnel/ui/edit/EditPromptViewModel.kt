package com.isaacshub.app.featurefunnel.ui.edit

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.isaacshub.app.featurefunnel.data.DiscordChannel
import com.isaacshub.app.featurefunnel.data.FeatureFunnelApiClient
import com.isaacshub.app.featurefunnel.data.FeatureFunnelRepository
import com.isaacshub.app.vault.domain.VaultConnection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class EditPromptUiState(
    val title: String = "",
    val promptText: String = "",
    val priority: Int = 0,
    val selectedChannelId: String = "",
    val availableChannels: List<DiscordChannel> = emptyList(),
    val isLoadingChannels: Boolean = false,
    val canSave: Boolean = false
)

class EditPromptViewModel(
    private val repository: FeatureFunnelRepository,
    private val promptId: Long?,
    private val getConnection: suspend () -> VaultConnection?
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditPromptUiState())
    val uiState: StateFlow<EditPromptUiState> = _uiState.asStateFlow()

    init {
        loadChannels()
        if (promptId != null) {
            loadPrompt(promptId)
        }
    }

    private fun loadChannels() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingChannels = true)
            val connection = getConnection()
            if (connection != null) {
                val apiClient = FeatureFunnelApiClient(connection)
                val channels = apiClient.getAvailableChannels()
                _uiState.value = _uiState.value.copy(
                    availableChannels = channels,
                    isLoadingChannels = false
                )
            } else {
                _uiState.value = _uiState.value.copy(isLoadingChannels = false)
            }
        }
    }

    private fun loadPrompt(id: Long) {
        viewModelScope.launch {
            val prompt = repository.getById(id)
            if (prompt != null) {
                _uiState.value = _uiState.value.copy(
                    title = prompt.title,
                    promptText = prompt.promptText,
                    priority = prompt.priority,
                    selectedChannelId = prompt.channelId,
                    canSave = true
                )
            }
        }
    }

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(
            title = title,
            canSave = title.isNotBlank() && _uiState.value.promptText.isNotBlank() && _uiState.value.selectedChannelId.isNotBlank()
        )
    }

    fun updatePromptText(text: String) {
        _uiState.value = _uiState.value.copy(
            promptText = text,
            canSave = _uiState.value.title.isNotBlank() && text.isNotBlank() && _uiState.value.selectedChannelId.isNotBlank()
        )
    }

    fun updatePriority(priority: Int) {
        _uiState.value = _uiState.value.copy(priority = priority)
    }

    fun updateSelectedChannel(channelId: String) {
        _uiState.value = _uiState.value.copy(
            selectedChannelId = channelId,
            canSave = _uiState.value.title.isNotBlank() && _uiState.value.promptText.isNotBlank() && channelId.isNotBlank()
        )
    }

    fun save() {
        viewModelScope.launch {
            val state = _uiState.value
            if (promptId == null) {
                repository.createPrompt(
                    title = state.title,
                    promptText = state.promptText,
                    channelId = state.selectedChannelId,
                    priority = state.priority
                )
            } else {
                val existing = repository.getById(promptId)
                if (existing != null) {
                    repository.updatePrompt(
                        existing.copy(
                            title = state.title,
                            promptText = state.promptText,
                            channelId = state.selectedChannelId,
                            priority = state.priority
                        )
                    )
                }
            }
        }
    }

    class Factory(
        private val repository: FeatureFunnelRepository,
        private val promptId: Long?,
        private val getConnection: suspend () -> VaultConnection?
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            EditPromptViewModel(repository, promptId, getConnection) as T
    }
}
