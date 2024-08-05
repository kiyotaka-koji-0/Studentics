package com.studentics.kiyo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.studentics.kiyo.components.BottomNavBar
import com.studentics.kiyo.components.TopBar



@Composable
fun HomeScreenUI(navController: NavController) {
    Scaffold(topBar = ({ TopBar() }), bottomBar = ({ BottomNavBar(navController = navController)})) { paddingValues->

        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {

        }
    }
}
