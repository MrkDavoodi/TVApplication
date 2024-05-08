package com.example.tvapplication.commons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.unit.dp

@Composable
fun CircularIndeterminateProgressBar(isDisplayed: Boolean) {
    if (isDisplayed) {
        Column(
            modifier = Modifier.fillMaxSize() ,
            horizontalAlignment = Alignment.CenterHorizontally ,
            verticalArrangement = Arrangement.Center//.background(Color.Black.copy(alpha = 0.5f)),
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(45.dp),
                color = Blue
            )
        }

    }
}