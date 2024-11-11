package com.example.simplerick.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.network.KtorClient
import com.example.network.models.domain.Character

@Composable
fun CharacterDetailScreen(ktorClient: KtorClient, characterId: Int) {

    var character by remember { mutableStateOf<Character?>(null) }

//    val characterDataPoints by remember {
//        derivedStateOf {
//            buildList {
//                character?.let { character ->
//                    add(DataPoint)
//                }
//            }
//        }
//    }

}