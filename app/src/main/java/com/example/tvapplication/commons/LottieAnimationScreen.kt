package com.example.tvapplication.commons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.tv.material3.Text


@Composable
fun LottieAnimationScreen(isPlaying: Boolean) {

//    val composition by rememberLottieComposition(
//        spec = LottieCompositionSpec.RawRes(R.raw.loading)
//    )
//    val progress: Float by animateLottieCompositionAsState(
//        composition = composition,
//        isPlaying = isPlaying, // this variable is driven by what ever your requirements are (mutableStateOf / viewmodel state, etc.)
//        restartOnPlay = true,
//        iterations = LottieConstants.IterateForever,
//        cancellationBehavior = LottieCancellationBehavior.OnIterationFinish
//    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

//        LottieAnimation(
//            modifier = Modifier.size(80.dp),
//            composition = composition,
//            progress = progress
//        )
        Text(text = "لطفا شکیبا باشید... ", color = Color.White)

    }
}
