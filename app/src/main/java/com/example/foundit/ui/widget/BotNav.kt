package com.example.foundit.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.foundit.R
import com.example.foundit.data.NavStateItem
import com.example.foundit.ui.theme.poppinsFamily
import com.example.foundit.ui.viewmodel.BotNavViewModel

@Composable
fun BotNav(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: BotNavViewModel,
    fabClick: () -> Unit = {}
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val selectedItem = viewModel.selectedItem

    val navItems = listOf(
        NavStateItem(
            route = "home_screen",
            title = "Beranda",
            selectedIcon = {
                Icon(
                    modifier = Modifier
                        .padding(1.dp)
                        .width(35.dp)
                        .height(35.dp),
                    painter = painterResource(id = R.drawable.selected_home),
                    contentDescription = "Icon Description",
                    tint = Color(0xFF193A6F)
                )
            },
            unselectedIcon = {
                Icon(
                    modifier = Modifier
                        .padding(1.dp)
                        .width(35.dp)
                        .height(35.dp),
                    painter = painterResource(id = R.drawable.unselected_home),
                    contentDescription = "Icon Description",
                    tint = Color(0xFF6F6F6F)
                )
            }
        ),
        NavStateItem(
            route = "profile_screen",
            title = "Profile",
            selectedIcon = {
                Icon(
                    modifier = Modifier
                        .padding(1.dp)
                        .width(0.dp)
                        .height(0.dp),
                    painter = painterResource(id = R.drawable.selected_profile),
                    contentDescription = "Icon Description",
                    tint = Color(0xFF193A6F)
                )
            },
            unselectedIcon = {
                Icon(
                    modifier = Modifier
                        .padding(1.dp)
                        .width(0.dp)
                        .height(0.dp),
                    painter = painterResource(id = R.drawable.unselected_profile),
                    contentDescription = "Icon Description",
                    tint = Color(0xFF6F6F6F)
                )
            }
        ),
        NavStateItem(
            route = "profile_screen",
            title = "Akun",
            selectedIcon = {
                Icon(
                    modifier = Modifier
                        .padding(1.dp)
                        .width(35.dp)
                        .height(35.dp),
                    painter = painterResource(id = R.drawable.selected_profile),
                    contentDescription = "Icon Description",
                    tint = Color(0xFF193A6F)
                )
            },
            unselectedIcon = {
                Icon(
                    modifier = Modifier
                        .padding(1.dp)
                        .width(35.dp)
                        .height(35.dp),
                    painter = painterResource(id = R.drawable.unselected_profile),
                    contentDescription = "Icon Description",
                    tint = Color(0xFF6F6F6F)
                )
            }
        )
    )

    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(0.dp),
        ) {
            navItems.forEachIndexed { index, item ->

                NavigationBarItem(
                    modifier = Modifier
                        .padding(0.dp),
                    selected = selectedItem == index,
                    onClick = {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route)
                        }
                        viewModel.updateSelectedItem(index)
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.White,
                        selectedTextColor = Color(0xFF193A6F),
                        unselectedTextColor = Color(0xFF6F6F6F)
                    ),
                    icon =
                    if (selectedItem == index) item.selectedIcon else item.unselectedIcon,
                    label = {
                        if(index != 1) {
                            Text(
                                text = item.title,
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = poppinsFamily,
                                    fontWeight = FontWeight(500)
                                )
                            )
                        }
                    },
                    enabled = if (index != 1) true else false
                )
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.Center)
                .size(55.dp)
                .offset(y = (-40).dp),
            onClick = {
                fabClick()
            },
            shape = RoundedCornerShape(100f),
            containerColor = Color.White
        ) {
            Image(
                painter = painterResource(id = R.drawable.laporbutton_icon),
                contentDescription = "lapor button",
                contentScale = ContentScale.None
            )
        }
    }
}