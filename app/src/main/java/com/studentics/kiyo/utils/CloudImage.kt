package com.studentics.kiyo.utils

import android.graphics.Color
import android.widget.Spinner
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import coil.Coil
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.studentics.kiyo.R


@Composable
fun LoadImage(imageUrl: String, modifier: Modifier = Modifier, shapes: Shape = RectangleShape) {

    SubcomposeAsyncImage(
        model = imageUrl,

        contentDescription = "Image description",
        modifier = modifier,
        loading = {
            CircularProgressIndicator()
        }
    )

}


