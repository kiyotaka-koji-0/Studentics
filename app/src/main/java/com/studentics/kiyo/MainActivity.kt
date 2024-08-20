package com.studentics.kiyo

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.studentics.kiyo.classes.AddExamScreen
import com.studentics.kiyo.classes.HomeScreen
import com.studentics.kiyo.classes.SplashScreen
import com.studentics.kiyo.screens.AddExamScreenUI
import com.studentics.kiyo.screens.HomeScreenUI
import com.studentics.kiyo.screens.SplashScreenUI
import com.studentics.kiyo.ui.theme.StudenticsTheme
import com.studentics.kiyo.viewModels.ExamViewModel
import com.studentics.kiyo.viewModels.FirestoreRepository

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val firestore = Firebase.firestore
        setContent {
            StudenticsTheme {
                val navController = rememberNavController()
                val navHost = NavHost(navController = navController, startDestination = SplashScreen){
                    composable<SplashScreen>{
                        SplashScreenUI(navController=navController)
                    }
                    composable<HomeScreen>{
                        val fireStoreRepo = FirestoreRepository()
                        val viewModel = ExamViewModel(fireStoreRepo)
                        HomeScreenUI(navController, viewModel)
                    }
                    
                    composable<AddExamScreen> { 
                        AddExamScreenUI(navController = navController)
                    }
                }
            }
        }
    }
}

