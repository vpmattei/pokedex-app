package com.example.pokedexapp.pokemondetail

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.pokedexapp.data.remote.responses.Pokemon
import com.example.pokedexapp.repository.PokemonRepository
import com.example.pokedexapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _pokemonInfo = mutableStateOf<Resource<Pokemon>>(Resource.Loading())
    val pokemonInfo: State<Resource<Pokemon>> = _pokemonInfo

    private val _previousPokemon = mutableStateOf<Pokemon?>(null)
    val previousPokemon: State<Pokemon?> = _previousPokemon

    private val _nextPokemon = mutableStateOf<Pokemon?>(null)
    val nextPokemon: State<Pokemon?> = _nextPokemon

    private val _previousPokemonColor = mutableStateOf<Color?>(null)
    val previousPokemonColor: State<Color?> = _previousPokemonColor

    private val _nextPokemonColor = mutableStateOf<Color?>(null)
    val nextPokemonColor: State<Color?> = _nextPokemonColor

    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon> {
        return repository.getPokemonInfo(pokemonName)
    }

    fun loadPreviousAndNextPokemons(currentPokemonId: Int) {
        viewModelScope.launch {
            val previousResult = withContext(Dispatchers.IO) {
                repository.getPokemonInfo((currentPokemonId - 1).toString())
            }
            val nextResult = withContext(Dispatchers.IO) {
                repository.getPokemonInfo((currentPokemonId + 1).toString())
            }

            _previousPokemon.value = if (previousResult is Resource.Success) previousResult.data else null
            _nextPokemon.value = if (nextResult is Resource.Success) nextResult.data else null

            previousResult.data?.sprites?.front_default?.let { imageUrl ->
                _previousPokemonColor.value = calcDominantColorFromUrl(imageUrl)
            }

            nextResult.data?.sprites?.front_default?.let { imageUrl ->
                _nextPokemonColor.value = calcDominantColorFromUrl(imageUrl)
            }
        }
    }

    fun getCachedPreviousPokemon(): Pokemon? {
        return previousPokemon.value
    }

    fun getCachedNextPokemon(): Pokemon? {
        return nextPokemon.value
    }

    fun getCachedPreviousPokemonColor(): Color? {
        return previousPokemonColor.value
    }

    fun getCachedNextPokemonColor(): Color? {
        return nextPokemonColor.value
    }

    private suspend fun calcDominantColorFromUrl(imageUrl: String): Color {
        return withContext(Dispatchers.IO) {
            try {
                val bitmap = loadImageBitmapFromUrl(imageUrl)
                extractDominantColor(bitmap)
            } catch (e: Exception) {
                Color.Gray // Default fallback color
            }
        }
    }

    private suspend fun loadImageBitmapFromUrl(imageUrl: String): Bitmap {
        return withContext(Dispatchers.IO) {
            val connection = URL(imageUrl).openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            BitmapFactory.decodeStream(inputStream)
        }
    }

    private suspend fun extractDominantColor(bitmap: Bitmap): Color {
        return withContext(Dispatchers.Default) {
            Palette.from(bitmap).generate().dominantSwatch?.rgb?.let { Color(it) } ?: Color.Gray
        }
    }
}