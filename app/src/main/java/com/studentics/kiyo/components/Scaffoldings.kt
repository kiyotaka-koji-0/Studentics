package com.studentics.kiyo.components

import androidx.annotation.OpenForTesting
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.studentics.kiyo.R
import com.studentics.kiyo.classes.navItems
import com.studentics.kiyo.utils.getFont
import androidx.compose.runtime.remember as remember


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(title = ({ Text(text = "Studentics", fontFamily = getFont("Ubuntu"), color = colorResource(
        id = R.color.google_green
    ))}), colors = TopAppBarColors(
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
    NavigationBar(
        containerColor = colorResource(id = R.color.metalBG),
        contentColor = Color.White
    ) {
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

@Composable
fun CustomSnackbar(
    message: String,
    actionLabel: String? = null,
    onActionClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    duration: SnackbarDuration = SnackbarDuration.Short
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Show the Snackbar
    LaunchedEffect(snackbarHostState) {
        snackbarHostState.showSnackbar(
            message = message,
            actionLabel = actionLabel,
            duration = duration
        )
    }

    // Customize the Snackbar appearance
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier,
        snackbar = { snackbarData ->
            Snackbar(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .background(
                        color = colorResource(id = R.color.google_green),
                        shape = RoundedCornerShape(8.dp)
                    ),

                contentColor = Color.White,
                shape = RoundedCornerShape(8.dp),
                actionOnNewLine = false
            ) {
                Text(
                    text = snackbarData.visuals.message,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = getFont("Poppins")
                    )
                )
                snackbarData.visuals.actionLabel?.let { action ->
                    TextButton(onClick = { onActionClick() }) {
                        Text(
                            text = action,
                            style = TextStyle(
                                color = colorResource(id = R.color.teal_200),
                                fontSize = 16.sp,
                                fontFamily = getFont("Ubuntu")
                            )
                        )
                    }
                }
            }
        }
    )
}
