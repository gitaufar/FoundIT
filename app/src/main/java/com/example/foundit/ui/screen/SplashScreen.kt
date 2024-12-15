package com.example.foundit.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foundit.R
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.foundit.ui.theme.hellixFamily
import com.example.foundit.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navController: NavController? = null,
    homeViewModel: HomeViewModel
) {
    LaunchedEffect(Unit) {
        homeViewModel.initBarangHilang()
        homeViewModel.initBarangTemuan()
        navController!!.navigate("login_screen") {
            popUpTo("splash") { inclusive = true } // Menghapus SplashScreen dari backstack
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF193A6f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp, 200.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(50.dp)
                    )
                    .clip(RoundedCornerShape(50.dp))
                    .background(color = Color.White),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.foundit),
                    contentDescription = "logo",
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(26.dp))
            Row{
                Text(
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 50.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = hellixFamily,
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(0f, 4f),
                            blurRadius = 10f
                        )
                    ),
                    text = "Found",
                    )
                Text(
                    style = TextStyle(
                        color = Color(0xFFFF5C00),
                        fontSize = 50.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = hellixFamily,
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(0f, 4f),
                            blurRadius = 10f
                        )
                    ),
                    text = " It!",
                )
            }
            
        }
    }
}
