package com.example.simplerick.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplerick.repositories.EpisodeRepository
import com.example.simplerick.screens.AllEpisodesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllEpisodesViewModel @Inject constructor(private val repository: EpisodeRepository) :
    ViewModel() {

    private var _uiState = MutableStateFlow<AllEpisodesUiState>(AllEpisodesUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun refreshAllEpisodes(forceRefresh: Boolean = false) = viewModelScope.launch {
        if (forceRefresh) _uiState.update { AllEpisodesUiState.Loading }
        repository.fetchAllEpisodes().onSuccess { episodeList ->
            _uiState.update {
                AllEpisodesUiState.Success(data = episodeList.groupBy { it.seasonNumber.toString() }
                    .mapKeys { "Season ${it.key}" })
            }
        }.onFailure {
            //todo
        }
    }
}