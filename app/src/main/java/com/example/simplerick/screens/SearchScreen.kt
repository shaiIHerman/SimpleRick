package com.example.simplerick.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.delete
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.simplerick.components.character.CharacterListItem
import com.example.simplerick.components.common.DataPoint
import com.example.simplerick.components.common.SimpleToolbar
import com.example.simplerick.ui.theme.RickAction
import com.example.simplerick.ui.theme.RickPrimary
import com.example.simplerick.viewmodels.SearchViewModel

@Composable
fun SearchScreen(searchViewModel: SearchViewModel = hiltViewModel()) {
    DisposableEffect(key1 = Unit) {
        val job = searchViewModel.observeUserSearch()
        onDispose { job.cancel() }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        SimpleToolbar(title = "Search")

        val screenState by searchViewModel.uiState.collectAsStateWithLifecycle()

        AnimatedVisibility(visible = screenState is SearchViewModel.ScreenState.Searching) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = RickAction
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(color = Color.White, shape = RoundedCornerShape(4.dp))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Search icon",
                    tint = RickPrimary
                )
                BasicTextField(
                    state = searchViewModel.searchTextFieldState, modifier = Modifier.weight(1f)
                )
            }
            AnimatedVisibility(visible = searchViewModel.searchTextFieldState.text.isNotBlank()) {
                Icon(imageVector = Icons.Rounded.Delete,
                    contentDescription = "Delete icon",
                    tint = RickAction,
                    modifier = Modifier.clickable {
                        searchViewModel.searchTextFieldState.edit { delete(0, length) }
                    })
            }
        }

        when (val state = screenState) {
            SearchViewModel.ScreenState.Empty -> {
                Text(
                    text = "Search for characters!",
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 32.sp
                )
            }

            SearchViewModel.ScreenState.Searching -> {}

            is SearchViewModel.ScreenState.Content -> SearchScreenContent(state)

            is SearchViewModel.ScreenState.Error -> {
                Text(
                    text = state.message,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 26.sp
                )

                Button(colors = ButtonDefaults.buttonColors().copy(containerColor = RickAction),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(84.dp),
                    onClick = { searchViewModel.searchTextFieldState.clearText() }) {
                    Text(text = "Clear search", color = RickPrimary)
                }
            }
        }

    }
}

@Composable
fun SearchScreenContent(content: SearchViewModel.ScreenState.Content) {
    Text(
        text = "${content.results.size} results for ${content.userQuery}",
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        textAlign = TextAlign.Center,
        fontSize = 22.sp
    )
    Box {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp)
        ) {
            items(content.results) { character ->
                val dataPoints = buildList {
                    add(DataPoint("Last known location", character.location.name))
                    add(DataPoint("Species", character.species))
                    add(DataPoint("Gender", character.gender.displayName))
                    character.type.takeIf { it.isNotEmpty() }?.let { type ->
                        add(DataPoint("Type", type))
                    }
                    add(DataPoint("Origin", character.origin.name))
                    add(DataPoint("Episode count", character.episodeIds.size.toString()))
                }
                CharacterListItem(character = character,
                    characterDataPoints = dataPoints,
                    onClick = {

                    })
            }
        }
        Spacer(
            modifier = Modifier
                .height(8.dp)
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            RickPrimary, Color.Transparent
                        )
                    )
                )
        )
    }
}
