package com.studentics.kiyo.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.studentics.kiyo.classes.HomeScreen
import com.studentics.kiyo.utils.LoadImage
import com.studentics.kiyo.utils.getFont
import kotlinx.coroutines.delay


@Composable
fun SplashScreenUI(navController: NavController){
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
        delay(1000)
        navController.navigate(HomeScreen)
    }

    AnimatedVisibility(visible = visible, enter = fadeIn(), exit = fadeOut()) {


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
        ) {
            LoadImage(
                imageUrl = "https://lh3.googleusercontent.com/a/ACg8ocLI-5HY5TgkaAd3IKO3DQ_jiAYP0YG6jcd7l6tj7YaPHWe3awI=s288-c-no",
                modifier = Modifier
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Welcome Kiyotaka",
                color = Color.White,
                fontSize = 25.sp,
                fontFamily = getFont("Ubuntu")
            )

        }
    }
}