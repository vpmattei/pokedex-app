package com.example.pokedexapp.pokemondetail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.pokedexapp.R
import com.example.pokedexapp.data.remote.responses.Pokemon
import com.example.pokedexapp.ui.theme.*
import com.example.pokedexapp.util.Resource
import java.util.Locale

@Composable
fun PokemonDetailScreen(
    dominantColor: Color,
    pokemonName: String,
    navController: NavController,
    topPadding: Dp = 20.dp,
    pokemonImageSize: Dp = 200.dp,
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {
    val pokemonInfo = produceState<Resource<Pokemon>>(initialValue = Resource.Loading()) {
        value = viewModel.getPokemonInfo(pokemonName)
    }.value
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dominantColor)
            .padding(
                bottom = 16.dp
            )
    ) {
        PokemonDetailTopSection(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.2f)
                .align(Alignment.TopCenter)
        )
        PokemonDetailStateWrapper(
            pokemonInfo = pokemonInfo,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding + pokemonImageSize / 2,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surface)
                .align(Alignment.BottomCenter),
            loadingModifier = Modifier
                .size(100.dp)
                .align(Center)
                .padding(
                    top = topPadding + pokemonImageSize / 2,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        )
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize(),
        ) {
            if (pokemonInfo is Resource.Success) {
                pokemonInfo.data?.sprites?.let {
                    SubcomposeAsyncImage(
                        model = it.front_default,
                        contentDescription = pokemonInfo.data.name,
                        modifier = Modifier
                            .size(pokemonImageSize)
                            .offset(y = topPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PokemonDetailTopSection(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.2f)
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.Black,
                        Color.Transparent
                    )
                )
            )
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(36.dp)
                .offset(16.dp, 64.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}

@Composable
fun PokemonDetailStateWrapper(
    pokemonInfo: Resource<Pokemon>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier
) {
    when (pokemonInfo) {
        is Resource.Success -> {
            var pokemon: String = ""
            var pokemonColors: List<Color>
            for (type in pokemonInfo.data!!.types) {
                if (pokemon.isEmpty()) {
                    pokemon = type.type.name.capitalize(Locale.ROOT)
                } else {
                    pokemon = pokemon + " | " + type.type.name.capitalize(Locale.ROOT)
                }
            }
            Column(
                modifier = modifier
                    .padding(
                        top = pokemonInfo.data.sprites.front_default.length.dp + 20.dp
                    )

            ) {
                Text(
                    text = "#" +
                            pokemonInfo.data.id.toString() +
                            " " +
                            pokemonInfo.data.name.capitalize(Locale.ROOT),
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color =
                        if(isSystemInDarkTheme()) {
                            MaterialTheme.colorScheme.onSurface
                        } else Color.Black,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                )
                Spacer(
                    Modifier
                        .height(
                            16.dp
                        )
                )
                Box(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp
                        )
                        .fillMaxWidth(),
                    contentAlignment = Center,
                ) {
                    Column(

                    ) {
                        PokemonDetailTypesBar(pokemonInfo = pokemonInfo)
                        Spacer(Modifier.height(16.dp))
                        PokemonDetailDataSection(
                            pokemonWeight = pokemonInfo.data.weight,
                            pokemonHeight = pokemonInfo.data.height,
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = "Base stats:",
                            fontSize = 22.sp
                        )
                        Spacer(Modifier.height(8.dp))
                        val maxBaseStat = pokemonInfo.data.stats.maxOf { it.base_stat }
                        pokemonInfo.data.stats.forEach { stat ->
                            PokemonDetailStatsBar(
                                statName = stat.stat.name,
                                statValue = stat.base_stat,
                                statMaxValue = maxBaseStat,
                            )
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
            }
        }

        is Resource.Error -> {
            Text(
                text = pokemonInfo.message!!,
                color = Color.Red,
                modifier = modifier
            )
        }

        is Resource.Loading -> {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = loadingModifier
            )
        }
    }
}

@Composable
fun PokemonDetailTypesBar(
    pokemonInfo: Resource<Pokemon>,
) {
    val shape = RoundedCornerShape(32.dp)
    Row(
        Modifier
            .padding(
                start = 16.dp,
                end = 16.dp
            )
            .fillMaxWidth()
            .height(32.dp)
            .clip(shape)
            .border(0.dp, Color.Transparent, shape)
            .background(Color.Gray),
        horizontalArrangement = Arrangement.Center
    ) {
        pokemonInfo.data!!.types.forEachIndexed { index, type ->
            val typeName = type.type.name.capitalize(Locale.ROOT)
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(color = FindMatchingTypeColorFor(typeName))
                    .weight(.5f)
                    .clip(CircleShape),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = typeName,
                    textAlign = TextAlign.Center,
                    color = Color.White,

                    )
            }
        }
    }
}

@Composable
fun PokemonDetailStatsBar(
    statName: String,
    statValue: Int,
    statMaxValue: Int,
    animDuration: Int = 1000,
    animDelay: Int = 0
) {
    val shape = RoundedCornerShape(32.dp)
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val curPercentage = animateFloatAsState(
        targetValue = if(animationPlayed) {
            statValue / statMaxValue.toFloat()
        } else 0f,
        animationSpec = tween(
            animDuration,
            animDelay
        )
    )
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Box(
        Modifier
            .fillMaxWidth()
            .height(30.dp)
            .clip(shape)
            .border(0.dp, Color.Transparent, shape)
            .background(
                if (isSystemInDarkTheme()) {
                    Color(0xFF505050)
                } else {
                    Color.LightGray
                }
            )
    ) {
        Row(
            modifier = Modifier
                .clip(CircleShape)
                .fillMaxHeight()
                .fillMaxWidth(curPercentage.value)
                .background(color = FindMatchingStatColorFor(statName))
                .padding(
                    start = 8.dp,
                    end = 8.dp
                )
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        )  {
            Text(
                text = FindMatchingStatNamerFor(statName),
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = statValue.toString(),
            )
        }
    }
}

@Composable
fun PokemonDetailDataSection(
    pokemonWeight: Int,
    pokemonHeight: Int,
    sectionHeight: Dp = 80.dp
) {
    val pokemonWeightInKg = remember {
        pokemonWeight.toFloat()/10
    }
    val pokemonHeightInMeters = remember {
        pokemonHeight.toFloat()/10
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        PokemonDetailDataItem(
            dataValue = pokemonWeightInKg,
            dataUnit = "kg",
            dataIcon = painterResource(R.drawable.ic_weight),
            modifier = Modifier
                .weight(1f)
        )
        VerticalDivider(
            thickness = 1.dp,
            color = Color.LightGray,
            modifier = Modifier
                .height(sectionHeight)
        )
        PokemonDetailDataItem(
            dataValue = pokemonHeightInMeters,
            dataUnit = "m",
            dataIcon = painterResource(R.drawable.ic_height),
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Composable
fun PokemonDetailDataItem(
    dataValue: Float,
    dataUnit: String,
    dataIcon: Painter,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Icon(painter = dataIcon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
        Spacer(Modifier.height(8.dp))
        Text(
            text = "$dataValue$dataUnit",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

fun FindMatchingTypeColorFor(colorName: String): Color {
    when (colorName.lowercase()) {
        "normal" -> return TypeNormal
        "fire" -> return TypeFire
        "water" -> return TypeWater
        "electric" -> return TypeElectric
        "grass" -> return TypeGrass
        "ice" -> return TypeIce
        "fighting" -> return TypeFighting
        "poison" -> return TypePoison
        "ground" -> return TypeGround
        "flying" -> return TypeFlying
        "psychic" -> return TypePsychic
        "bug" -> return TypeBug
        "rock" -> return TypeRock
        "ghost" -> return TypeGhost
        "dragon" -> return TypeDragon
        "dark" -> return TypeDark
        "steel" -> return TypeSteel
        "fairy" -> return TypeFairy
        else -> return TypeNormal
    }
}

fun FindMatchingStatColorFor(statName: String): Color {
    when (statName.lowercase()) {
        "hp" -> return HPColor
        "attack" -> return AtkColor
        "defense" -> return DefColor
        "special-attack" -> return SpAtkColor
        "special-defense" -> return SpDefColor
        "speed" -> return SpdColor
        else -> return Color.Transparent
    }
}
fun FindMatchingStatNamerFor(statName: String): String {
    when (statName.lowercase()) {
        "hp" -> return "HP"
        "attack" -> return "Atk"
        "defense" -> return "Def"
        "special-attack" -> return "SpAtk"
        "special-defense" -> return "SpDef"
        "speed" -> return "Spd"
        else -> return "NULL"
    }
}