package com.example.simplerick.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.models.domain.CharacterPage
import com.example.simplerick.repositories.CharacterRepository
import com.example.simplerick.screens.HomeScreenViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val characterRepository: CharacterRepository) :
    ViewModel() {

    private val _viewState = MutableStateFlow<HomeScreenViewState>(HomeScreenViewState.Loading)
    val viewState: StateFlow<HomeScreenViewState> = _viewState.asStateFlow()

    private val fetchedCharactersPages = mutableListOf<CharacterPage>()

    fun fetchInitialPage() = viewModelScope.launch {
        val initialPage = characterRepository.fetchCharacterPage(page = 1)
        initialPage.onSuccess { characterPage ->
            fetchedCharactersPages.clear()
            fetchedCharactersPages.add(characterPage)
            _viewState.update { return@update HomeScreenViewState.GridDisplay(characters = characterPage.characters) }

        }.onFailure { //todo
        }
    }

    fun fetchNextPage() = viewModelScope.launch {
        val nextPageIndex = fetchedCharactersPages.size + 1
        val nextPage = characterRepository.fetchCharacterPage(page = nextPageIndex)
        nextPage.onSuccess { characterPage ->
            fetchedCharactersPages.add(characterPage)
            _viewState.update { currentState ->
                val currentCharacters =
                    (currentState as? HomeScreenViewState.GridDisplay)?.characters ?: emptyList()
                return@update HomeScreenViewState.GridDisplay(characters = currentCharacters + characterPage.characters)
            }
        }.onFailure {
            //todo
        }
    }
}