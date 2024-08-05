package com.studentics.kiyo.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import com.studentics.kiyo.R
import com.studentics.kiyo.classes.navItems
import com.studentics.kiyo.utils.getFont


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(title = ({ Text(text = "Studentics", fontFamily = getFont("Ubuntu"))}), colors = TopAppBarColors(
        containerColor = colorResource(id = R.color.metalBG), navigationIconContentColor = Color.White, titleContentColor = Color.White, actionIconContentColor = Color.White,
        scrolledContainerColor = Color.White
    ),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.1f))
}


@Composable
fun BottomNavBar(navController: NavController) {
    var selectedIndex by rememberSaveable { mutableStateOf(0) }
    NavigationBar() {
        navItems.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.selectedIcon, contentDescription = item.title) },
                label = { Text(text = item.title, fontFamily = getFont("Ubuntu")) },
                selected = selectedIndex == index,
                onClick = {
                    selectedIndex = index
                    navController.navigate(route = item.route)
                }
            )
        }
    }
}