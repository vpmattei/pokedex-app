package com.example.pokedexapp.data.remote.responses

data class PokemonList(
    val count: Int,
    val next: Any,
    val previous: Any,
    val results: List<Result>
)