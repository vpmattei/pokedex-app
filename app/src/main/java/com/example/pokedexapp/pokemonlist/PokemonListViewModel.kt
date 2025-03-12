package com.example.pokedexapp.pokemonlist

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.Image
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.palette.graphics.Palette
import com.example.pokedexapp.data.models.PokedexListEntry
import com.example.pokedexapp.repository.PokemonRepository
import com.example.pokedexapp.util.Constants.PAGE_SIZE
import com.example.pokedexapp.util.Resource
import com.example.pokedexapp.util.Resource.Error
import com.example.pokedexapp.util.Resource.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    val val_repository: PokemonRepository
): ViewModel() {

    private var curPage = 0

    var pokemonList = mutableStateOf<List<PokedexListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    init {
        loadPokemonPaginated()
    }

    fun loadPokemonPaginated() {
        viewModelScope.launch {
            isLoading.value = true
            val result = val_repository.getPokemonList(PAGE_SIZE, curPage * PAGE_SIZE)
            when(result) {
                is Success -> {
                    endReached.value = curPage * PAGE_SIZE >= result.data!!.count
                    val pokedexEntries = result.data.results.mapIndexed { index, entry ->
                        val number = if(entry.url.endsWith("/")) {
                            entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                        } else {
                            entry.url.takeLastWhile { it.isDigit() }
                        }
                        val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                        PokedexListEntry(entry.name.capitalize(Locale.ROOT), url, number.toInt())
                    }
                    curPage++

                    loadError.value = ""
                    isLoading.value = false
                    pokemonList.value += pokedexEntries
                }
                is Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
                else -> {
                    print("Else state reached")
                }
            }
        }
    }

    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}