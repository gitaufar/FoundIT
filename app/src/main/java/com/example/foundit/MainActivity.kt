package com.example.foundit

import LoginScreen
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.foundit.enumclass.Tipe
import com.example.foundit.ui.widget.BotNav
import com.example.foundit.ui.screen.HomeScreen
import com.example.foundit.ui.screen.LaporScreen
import com.example.foundit.ui.screen.RegisterScreen
import com.example.foundit.ui.screen.SplashScreen
import com.example.foundit.ui.theme.FoundITTheme
import com.example.foundit.ui.viewmodel.BotNavViewModel
import com.example.foundit.ui.viewmodel.HomeViewModel
import com.example.foundit.ui.widget.DetailedItemScreen
import kotlinx.coroutines.DelicateCoroutinesApi


class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(DelicateCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            FoundITTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    val navController = rememberNavController()
                    val botNavViewModel = BotNavViewModel()
                    val homeViewModel: HomeViewModel = viewModel()

                    NavHost(
                        navController = navController,
                        startDestination = "splash_screen"
                    ) {

                        composable(route = "splash_screen") {
                            SplashScreen(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController,
                                homeViewModel = homeViewModel
                            )
                        }

                        composable(route = "login_screen") {
                            LoginScreen(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController
                            )
                        }

                        composable(route = "register_screen") {
                            RegisterScreen(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController
                            )
                        }

                        composable(route = "home_screen") {
                            HomeScreen(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController,
                                botNavViewModel = botNavViewModel,
                                homeViewModel = homeViewModel
                            )
                        }

                        composable(route = "lapor_screen/{tipe}") {
                            val tipe = it.arguments?.getString("tipe")
                            val tipe_barang = Tipe.valueOf(tipe!!)
                            LaporScreen(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController,
                                tipe_barang = tipe_barang,
                                homeViewModel = homeViewModel
                            )
                        }

                        composable(route = "detail/{id}") {
                            val id = it.arguments?.getString("id")
                            DetailedItemScreen(
                                id_barang = (id ?: "0").toInt(),
                                navController = navController
                            )
                        }

                        composable(route = "profile_screen") {
                            Scaffold(
                                bottomBar = {
                                    BotNav(
                                        navController = navController!!,
                                        viewModel = botNavViewModel
                                    )
                                }
                            ) { innerPadding ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(color = Color.Blue)
                                        .padding(innerPadding),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("INI PROFILE", fontSize = 30.sp)
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}

