package com.roblescode.slider.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import com.google.accompanist.pager.*
import com.roblescode.slider.utils.getRandomImage
import kotlin.math.absoluteValue


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun MainScreen() {

    val state = rememberPagerState()

    Scaffold(topBar = {

    }, content = { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                HorizontalPager(
                    state = state,
                    count = 10,
                    itemSpacing = 24.dp
                ) { page ->
                    val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                    ImageCard(page = page + 1, pageOffset)
                }

                Spacer(modifier = Modifier.size(24.dp))

                HorizontalPagerIndicator(
                    pagerState = state,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp),
                )
            }
        }
    })
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageCard(page: Int, pageOffset: Float) {
    val image = rememberSaveable {
        mutableStateOf(getRandomImage())
    }

    val favorite = rememberSaveable {
        mutableStateOf(false)
    }


    Card(
        onClick = { image.value = getRandomImage() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = Modifier.graphicsLayer {
            // Calculate the absolute offset for the current page from the
            // scroll position. We use the absolute value which allows us to mirror
            // any effects for both directions


            // We animate the scaleX + scaleY, between 85% and 100%
            lerp(
                start = 0.85f,
                stop = 1f,
                fraction = 1f - pageOffset.coerceIn(0f, 1f)
            ).also { scale ->
                scaleX = scale
                scaleY = scale
            }

            // We animate the alpha, between 50% and 100%
            alpha = lerp(
                start = 0.5f,
                stop = 1f,
                fraction = 1f - pageOffset.coerceIn(0f, 1f)
            )
        }
    ) {
        Box(
            modifier = Modifier
                .width(300.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    model = image.value,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Image $page",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 24.sp
                            )
                            IconButton(onClick = { favorite.value = !favorite.value }) {
                                Icon(
                                    imageVector = if (favorite.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = null
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
                        Spacer(modifier = Modifier.size(8.dp))
                    }
                }
            }
        }
    }
}