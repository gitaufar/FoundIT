@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.foundit.ui.screen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogWindowProvider
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import com.example.foundit.R
import com.example.foundit.ui.widget.BotNav
import com.example.foundit.ui.theme.poppinsFamily
import com.example.foundit.ui.viewmodel.BotNavViewModel
import com.example.foundit.ui.viewmodel.HomeViewModel
import com.example.foundit.ui.widget.CardCategory
import com.example.foundit.ui.widget.CardItem
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController? = null,
    botNavViewModel: BotNavViewModel,
    homeViewModel: HomeViewModel,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val calendarState = rememberSheetState()
    var calendarOpen by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    var isFilter by remember { mutableStateOf(false) }
    var filtering by remember { mutableStateOf(false) }
    var isSearch by remember { mutableStateOf(false) }
    var isType by remember { mutableStateOf(false) }
    val temuan by homeViewModel.temuan.collectAsState(initial = true)
    val hilang by homeViewModel.hilang.collectAsState(initial = false)
    var kategori by remember { mutableStateOf("") }
    val listBarang by homeViewModel.listBarang.collectAsState(initial = emptyList())
    val searchState = remember { TextFieldState("") }
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    var dateResult by remember { mutableStateOf("dd/mm/yyyy") }
    var hourStart by remember { mutableStateOf("hh:mm") }
    var hourEnd by remember { mutableStateOf("hh:mm") }
    var hourStartOpen by remember { mutableStateOf(false) }
    var hourEndOpen by remember { mutableStateOf(false) }
    val hourStartState = rememberSheetState()
    val hourEndState = rememberSheetState()
    var openDialog by remember { mutableStateOf(false) }

    val filterCategory = remember {
        mutableStateListOf(
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0
        )
    }
    val lokasi = remember {
        mutableStateListOf(
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0
        )
    }

    val category = listOf(
        "Dompet",
        "Aksesoris",
        "Kunci",
        "Elektronik",
        "Smartphone",
        "Alat Tulis",
        "Botol",
        "Pakaian",
        "Lainnya"
    )

    val listLokasi = listOf(
        "Gedung F",
        "Gedung G",
        "Edutech",
        "Parkiran Gedung A",
        "Qopee",
        "GKM",
        "Junction",
        "Mushola",
        "Lainnya"
    )


    val categoryImage = listOf(
        painterResource(R.drawable.dompet),
        painterResource(R.drawable.aksesoris),
        painterResource(R.drawable.key),
        painterResource(R.drawable.elektronik),
        painterResource(R.drawable.smartphone),
        painterResource(R.drawable.alat_tulis),
        painterResource(R.drawable.botol),
        painterResource(R.drawable.pakaian),
        painterResource(R.drawable.lainnya)
    )

    if (filterCategory.contains(1) || lokasi.contains(1) || dateResult != "dd/mm/yyyy" || hourStart != "HH:mm" || hourEnd != "HH:mm") {
        filtering = true;
    } else {
        filtering = false;
    }

    LaunchedEffect(Unit) {
        homeViewModel.fetchBarangTemuan()
    }

    LaunchedEffect(filtering) {
        if (filtering) {
            sheetState.expand()
        }
    }

    Scaffold(
        bottomBar = {
            BotNav(
                navController = navController!!,
                viewModel = botNavViewModel,
                fabClick = {
                    openDialog = true
                }
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(innerPadding),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (isSearch) 100.dp else 172.dp)
                        .background(
                            color = if (searchState.text.isNotBlank() || isSearch) Color(
                                0xFF193A6F
                            ) else Color.White
                        )
                        .padding(bottom = if (searchState.text.isNotBlank() || isSearch) 16.dp else 0.dp)
                ) {

                    //tidak mencari(home screen biasa)
                    if (!isSearch) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .background(color = Color(0xFF193A6F))
                        )
                    }
                    Row(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        if (isSearch) {
                            Image(
                                modifier = Modifier
                                    .padding(1.dp)
                                    .width(30.dp)
                                    .height(30.dp)
                                    .clickable {
                                        isType = false
                                        isSearch = false
                                        focusManager.clearFocus()
                                        searchState.clearText()
                                    },
                                painter = painterResource(id = R.drawable.back),
                                contentDescription = "image description",
                                contentScale = ContentScale.None
                            )
                        }
                        BasicTextField(
                            modifier = Modifier
                                .size(
                                    if (searchState.text.isNotBlank() || isSearch) 272.dp else 360.dp,
                                    44.dp
                                )
                                .shadow(elevation = 10.dp, shape = RoundedCornerShape(10.dp))
                                .focusRequester(focusRequester)
                                .onFocusChanged { focusState ->
                                    isFocused = focusState.isFocused
                                    if (isFocused) {
                                        isSearch = true
                                        isType = true
                                    }
                                },
                            state = searchState,
                            decorator = { innerText ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(color = Color.White)
                                        .padding(horizontal = 8.dp, vertical = 0.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        modifier = Modifier
                                            .size(24.dp, 24.dp),
                                        painter = painterResource(R.drawable.search),
                                        contentDescription = null,
                                        tint = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        if (searchState.text.isEmpty()) {
                                            Text(
                                                "Cari barang",
                                                color = Color.Gray,
                                                fontFamily = poppinsFamily
                                            )
                                        }
                                        innerText()
                                        if (!isSearch) {
                                            Image(
                                                modifier = Modifier
                                                    .padding(1.dp)
                                                    .width(24.dp)
                                                    .height(24.dp)
                                                    .align(Alignment.CenterEnd)
                                                    .clickable {
                                                        scope
                                                            .launch {
                                                                sheetState.expand()
                                                            }
                                                            .invokeOnCompletion {
                                                                isFilter = true
                                                            }
                                                        Log.d("mencet filter", "filter terpencet")
                                                    },
                                                painter = painterResource(id = R.drawable.mage_filter),
                                                contentDescription = "image description",
                                                contentScale = ContentScale.None,
                                            )
                                        }
                                    }
                                }
                            }
                        )
                        if (isSearch && !isType) {
                            Box(
                                modifier = Modifier
                                    .width(44.dp)
                                    .height(44.dp)
                                    .background(
                                        color = Color(0xFFFFFFFF),
                                        shape = RoundedCornerShape(size = 10.dp)
                                    )
                                    .clickable {
                                        scope
                                            .launch {
                                                sheetState.expand()
                                            }
                                            .invokeOnCompletion {
                                                isFilter = true
                                            }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    modifier = Modifier
                                        .padding(1.dp)
                                        .width(30.dp)
                                        .height(30.dp),
                                    painter = painterResource(id = R.drawable.mage_filter),
                                    contentDescription = "image description",
                                    contentScale = ContentScale.None
                                )
                            }
                        }

                        if (isSearch && isType) {
                            Text(
                                modifier = Modifier
                                    .clickable {
                                        homeViewModel.fetchBarangSearch(searchState.text.toString())
                                        isType = false
                                        focusManager.clearFocus()
                                    },
                                text = "Cari",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = poppinsFamily,
                                    fontWeight = FontWeight(600),
                                    color = Color(0xFFFFFFFF),
                                )
                            )
                        }
                    }
                }

                if (isSearch && !isType) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 10.dp, start = 26.dp, end = 26.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .width(360.dp),
                            horizontalArrangement = Arrangement.spacedBy(
                                10.dp,
                                Alignment.CenterHorizontally
                            ),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(
                                modifier = Modifier
                                    .width(150.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    modifier = Modifier
                                        .clickable {
                                            homeViewModel.temuan()
                                            homeViewModel.fetchBarangSearch(
                                                searchState.text.toString()
                                            )
                                        },
                                    text = "Temuan",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontFamily = poppinsFamily,
                                        fontWeight = FontWeight(600),
                                        color = if (temuan) Color(0xFF193A6F) else Color(0xFF6F6F6F)
                                    )
                                )
                                if (temuan) {
                                    Spacer(
                                        Modifier
                                            .width(55.dp)
                                            .height(5.dp)
                                            .background(
                                                color = Color(0xFF08498E),
                                                shape = RoundedCornerShape(size = 20.dp)
                                            )
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .width(150.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    modifier = Modifier
                                        .clickable {
                                            homeViewModel.hilang()
                                            homeViewModel.fetchBarangSearch(
                                                searchState.text.toString()
                                            )
                                        },
                                    text = "Hilang",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontFamily = poppinsFamily,
                                        fontWeight = FontWeight(600),
                                        color = if (hilang) Color(0xFF193A6F) else Color(0xFF6F6F6F)
                                    )
                                )
                                if (hilang) {
                                    Spacer(
                                        Modifier
                                            .width(55.dp)
                                            .height(5.dp)
                                            .background(
                                                color = Color(0xFF08498E),
                                                shape = RoundedCornerShape(size = 20.dp)
                                            )
                                    )
                                }
                            }
                        }
                        LazyVerticalGrid(
                            modifier = Modifier
                                .padding(top = 16.dp, bottom = 12.dp),
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(15.dp)
                        ) {
                            items(listBarang.size) {
                                val item = listBarang[it]
                                CardItem(
                                    onItemClick = {
                                        navController!!.navigate("detail/${item.id}")
                                    },
                                    barang = item
                                )
                            }
                            item {
                                Spacer(modifier = Modifier.size(12.dp))
                            }
                        }
                    }
                }

                if (isSearch && isType) {

                }

                if (!isSearch) {
                    Column(
                        modifier = Modifier.padding(top = 12.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(start = 18.dp),
                            text = "Kategori Barang",
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            fontFamily = poppinsFamily
                        )
                        Spacer(Modifier.size(20.dp))
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            item {
                                Spacer(modifier = Modifier.size(18.dp))
                            }
                            items(category.size) { it ->
                                val text = category[it]
                                val image = categoryImage[it]
                                CardCategory(
                                    onClick = {
                                        var filter = text.uppercase()
                                        if (filter == kategori) {
                                            kategori = ""
                                            if (temuan) {
                                                homeViewModel.fetchBarangTemuan()
                                            } else {
                                                homeViewModel.fetchBarangHilang()
                                            }
                                        } else {
                                            kategori = filter
                                        }
                                    },
                                    image = image,
                                    category = text
                                )
                            }
                        }
                        Spacer(Modifier.size(20.dp))
                        Text(
                            modifier = Modifier
                                .padding(start = 18.dp),
                            text = "Terbaru di Sekitar",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontFamily = poppinsFamily
                        )
                    }
                    Row(
                        modifier = Modifier
                            .width(411.dp)
                            .height(60.dp)
                            .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(
                            25.dp,
                            Alignment.CenterHorizontally
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Button(
                            modifier = Modifier
                                .size(150.dp, 40.dp)
                                .clip(RoundedCornerShape(50.dp)),
                            onClick = {
                                homeViewModel.temuan()
                                homeViewModel.fetchBarangTemuan()
                            },
                            contentPadding = ButtonDefaults.ContentPadding,
                            colors = ButtonDefaults.buttonColors(if (temuan) Color(0xFF2C599D) else Color.White),
                            elevation = ButtonDefaults.buttonElevation(4.dp),
                            content = {
                                Text(
                                    "Temuan",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (temuan) Color.White else Color(0xFF193A6F),
                                    fontFamily = poppinsFamily
                                )
                            }
                        )
                        Button(
                            modifier = Modifier
                                .size(150.dp, 40.dp)
                                .clip(RoundedCornerShape(50.dp)),
                            onClick = {
                                homeViewModel.hilang()
                                homeViewModel.fetchBarangHilang()
                            },
                            contentPadding = ButtonDefaults.ContentPadding,
                            colors = ButtonDefaults.buttonColors(if (hilang) Color(0xFF2C599D) else Color.White),
                            elevation = ButtonDefaults.buttonElevation(4.dp),
                            content = {
                                Text(
                                    "Hilang",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (hilang) Color.White else Color(0xFF193A6F),
                                    fontFamily = poppinsFamily
                                )
                            }
                        )
                    }
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 10.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(
                            12.dp,
                            Alignment.CenterHorizontally
                        ),
                        verticalArrangement = Arrangement.spacedBy(16.dp)

                    ) {
                        //temp2
                        if (kategori.isNotBlank()) {
                            homeViewModel.fetchBarangCategory(kategori)
                        }
                        items(listBarang.size) {
                            val item = listBarang[it]
                            CardItem(
                                onItemClick = {
                                    navController!!.navigate("detail/${item.id}")
                                },
                                barang = item
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.size(12.dp))
                        }

                    }
                }
            }
            if (isFilter) {
                ModalBottomSheet(
                    sheetState = sheetState,
                    onDismissRequest = {
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            isFilter = false;
                            dateResult = "dd/mm/yyyy";
                            hourStart = "hh:mm";
                            hourEnd = "hh:mm";
                            filterCategory.forEachIndexed { index, _ ->
                                filterCategory[index] = 0
                            }
                            lokasi.forEachIndexed { index, _ ->
                                lokasi[index] = 0
                            }
                        }
                    },
                    containerColor = Color.White,
                ) {
                    val window = (LocalView.current.parent as? DialogWindowProvider)?.window
                    val view = LocalView.current
                    val windowInsetsController =
                        window?.let { WindowCompat.getInsetsController(it, view) }
                    DisposableEffect(view) {
                        windowInsetsController?.hide(WindowInsetsCompat.Type.navigationBars())
                        onDispose {
                            windowInsetsController?.hide(WindowInsetsCompat.Type.navigationBars())
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(
                                color = Color(0xFFFFFFFF),
                                shape = RoundedCornerShape(
                                    topStart = 25.dp,
                                    topEnd = 25.dp,
                                    bottomStart = 0.dp,
                                    bottomEnd = 0.dp
                                )
                            )
                            .padding(
                                start = 25.dp,
                                top = 0.dp,
                                end = 25.dp,
                                bottom = 26.dp
                            ), // Padding hanya pada sisi tertentu
                        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.Top),
                    ) {

                        Row(
                            modifier = Modifier
                                .height(35.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Filter",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = poppinsFamily,
                                    fontWeight = FontWeight(600),
                                    color = Color(0xFF000000),
                                )
                            )
                        }

                        Text(
                            text = "Kategori",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = poppinsFamily,
                                fontWeight = FontWeight(600),
                                color = Color(0xFF000000),
                            )
                        )

                        FlowRow(
                            modifier = Modifier
                                .padding(top = 5.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            category.forEachIndexed { index, kategori ->
                                Button(
                                    onClick = {
                                        filterCategory[index] = Math.abs(filterCategory[index] - 1)
                                    },
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .height(26.dp),
                                    shape = RoundedCornerShape(50.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        if (filterCategory[index] == 0) Color(
                                            0xFFE6ECF8
                                        ) else Color(0xFF2C599D)
                                    ),
                                    contentPadding = PaddingValues(
                                        horizontal = 16.dp,
                                        vertical = 4.dp
                                    ) // Padding untuk teks
                                ) {
                                    Text(
                                        text = kategori,
                                        style = TextStyle(
                                            fontSize = 12.sp,
                                            fontFamily = poppinsFamily,
                                            fontWeight = FontWeight.Medium,
                                            color = if (filterCategory[index] == 0) Color(0xFF193A6F) else Color.White,
                                            textAlign = TextAlign.Center
                                        )
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(
                            text = "Lokasi",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = poppinsFamily,
                                fontWeight = FontWeight(600),
                                color = Color(0xFF000000),
                            )
                        )
                        FlowRow(
                            modifier = Modifier
                                .padding(top = 5.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            listLokasi.forEachIndexed { index, lokasiText ->
                                Button(
                                    onClick = {
                                        lokasi[index] = Math.abs(lokasi[index] - 1)
                                    },
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .height(26.dp),
                                    shape = RoundedCornerShape(50.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        if (lokasi[index] == 0) Color(
                                            0xFFE6ECF8
                                        ) else Color(0xFF2C599D)
                                    ),
                                    contentPadding = PaddingValues(
                                        horizontal = 16.dp,
                                        vertical = 4.dp
                                    ) // Padding untuk teks
                                ) {
                                    Text(
                                        text = lokasiText,
                                        style = TextStyle(
                                            fontSize = 12.sp,
                                            fontFamily = poppinsFamily,
                                            fontWeight = FontWeight.Medium,
                                            color = if (lokasi[index] == 0) Color(0xFF193A6F) else Color.White,
                                            textAlign = TextAlign.Center
                                        )
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(
                            text = "Tanggal",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = poppinsFamily,
                                fontWeight = FontWeight(600),
                                color = Color(0xFF000000),
                            )
                        )
                        Button(
                            modifier = Modifier
                                .width(384.dp)
                                .wrapContentHeight(),
                            onClick = {
                                calendarOpen = true
                                calendarState.show()
                            },
                            shape = RoundedCornerShape(50.dp),
                            elevation = ButtonDefaults.buttonElevation(4.dp),
                            colors = ButtonDefaults.buttonColors(
                                if (dateResult == "dd/mm/yyyy") Color(0xFFFFFFFF) else Color(
                                    0xFFE6ECF8
                                )
                            ),
                            border = if (dateResult == "dd/mm/yyyy") null else BorderStroke(
                                width = 2.dp,
                                color = Color(0xFF2C599D)
                            ),
                            contentPadding = PaddingValues(start = 15.dp, end = 15.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(
                                    10.dp,
                                    Alignment.Start
                                ),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Image(
                                    modifier = Modifier
                                        .padding(1.dp)
                                        .width(28.dp)
                                        .height(28.dp),
                                    painter = painterResource(id = R.drawable.date),
                                    contentDescription = "image description",
                                    contentScale = ContentScale.None
                                )
                                Text(
                                    modifier = Modifier
                                        .width(260.dp),
                                    text = dateResult,
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        fontFamily = poppinsFamily,
                                        fontWeight = FontWeight(500),
                                        color = Color(0xFF6F6F6F),
                                    )
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(
                                    tint = Color(0xFF6F6F6F),
                                    modifier = Modifier
                                        .padding(1.dp)
                                        .width(22.dp)
                                        .height(22.dp),
                                    painter = painterResource(id = R.drawable.arrow_bottom),
                                    contentDescription = "image description",
                                )
                            }
                        }
                        Text(
                            text = "Waktu",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = poppinsFamily,
                                fontWeight = FontWeight(600),
                                color = Color(0xFF000000),
                            )
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(80.dp),
                                verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.Top),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    modifier = Modifier
                                        .width(175.dp),
                                    text = "Mulai",
                                    style = TextStyle(
                                        fontSize = 10.sp,
                                        fontFamily = poppinsFamily,
                                        fontWeight = FontWeight(500),
                                        color = Color(0xFF000000),
                                    ),
                                    textAlign = TextAlign.Start
                                )
                                Button(
                                    //jam mulai
                                    modifier = Modifier
                                        .width(175.dp)
                                        .wrapContentHeight(),
                                    onClick = {
                                        hourStartOpen = true
                                        hourStartState.show()
                                    },
                                    shape = RoundedCornerShape(50.dp),
                                    elevation = ButtonDefaults.buttonElevation(4.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        if (hourStart == "hh:mm") Color(0xFFFFFFFF) else Color(
                                            0xFFE6ECF8
                                        )
                                    ),
                                    border = if (hourStart == "hh:mm") null else BorderStroke(
                                        width = 2.dp,
                                        color = Color(0xFF2C599D)
                                    ),
                                    contentPadding = PaddingValues(start = 15.dp, end = 15.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(
                                            10.dp,
                                            Alignment.Start
                                        ),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Image(
                                            modifier = Modifier
                                                .padding(1.dp)
                                                .width(28.dp)
                                                .height(28.dp),
                                            painter = painterResource(id = R.drawable.waktu_detail),
                                            contentDescription = "image description",
                                            contentScale = ContentScale.None
                                        )
                                        Text(
                                            modifier = Modifier
                                                .width(260.dp),
                                            text = hourStart,
                                            style = TextStyle(
                                                fontSize = 12.sp,
                                                fontFamily = poppinsFamily,
                                                fontWeight = FontWeight(500),
                                                color = Color(0xFF6F6F6F),
                                            )
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        Icon(
                                            tint = Color(0xFF6F6F6F),
                                            modifier = Modifier
                                                .padding(1.dp)
                                                .width(22.dp)
                                                .height(22.dp),
                                            painter = painterResource(id = R.drawable.arrow_bottom),
                                            contentDescription = "image description",
                                        )
                                    }
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(80.dp),
                                verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.Top),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    modifier = Modifier
                                        .width(165.dp),
                                    text = "Selesai",
                                    style = TextStyle(
                                        fontSize = 10.sp,
                                        fontFamily = poppinsFamily,
                                        fontWeight = FontWeight(500),
                                        color = Color(0xFF000000),
                                    ),
                                    textAlign = TextAlign.Start
                                )
                                Button(
                                    //jam mulai
                                    modifier = Modifier
                                        .width(175.dp)
                                        .wrapContentHeight(),
                                    onClick = {
                                        hourEndOpen = true
                                        hourEndState.show()
                                    },
                                    shape = RoundedCornerShape(50.dp),
                                    elevation = ButtonDefaults.buttonElevation(4.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        if (hourEnd == "hh:mm") Color(0xFFFFFFFF) else Color(
                                            0xFFE6ECF8
                                        )
                                    ),
                                    border = if (hourEnd == "hh:mm") null else BorderStroke(
                                        width = 2.dp,
                                        color = Color(0xFF2C599D)
                                    ),
                                    contentPadding = PaddingValues(start = 15.dp, end = 15.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(
                                            10.dp,
                                            Alignment.Start
                                        ),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Image(
                                            modifier = Modifier
                                                .padding(1.dp)
                                                .width(28.dp)
                                                .height(28.dp),
                                            painter = painterResource(id = R.drawable.waktu_detail),
                                            contentDescription = "image description",
                                            contentScale = ContentScale.None
                                        )
                                        Text(
                                            modifier = Modifier
                                                .width(260.dp),
                                            text = hourEnd,
                                            style = TextStyle(
                                                fontSize = 12.sp,
                                                fontFamily = poppinsFamily,
                                                fontWeight = FontWeight(500),
                                                color = Color(0xFF6F6F6F),
                                            )
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        Icon(
                                            tint = Color(0xFF6F6F6F),
                                            modifier = Modifier
                                                .padding(1.dp)
                                                .width(22.dp)
                                                .height(22.dp),
                                            painter = painterResource(id = R.drawable.arrow_bottom),
                                            contentDescription = "image description",
                                        )
                                    }
                                }
                            }
                        }
                        if (filtering) {
                            Button(
                                modifier = Modifier
                                    .shadow(
                                        elevation = 16.100000381469727.dp,
                                        spotColor = Color(0x26000000),
                                        ambientColor = Color(0x26000000)
                                    )
                                    .width(362.dp),
                                onClick = {
                                    scope.launch {
                                        sheetState.hide()
                                    }.invokeOnCompletion {
                                        //filter barang
                                        val lokasiPick = ArrayList<String>()
                                        val categoryPick = ArrayList<String>()
                                        val dateFormat =
                                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                        val hourFormat =
                                            SimpleDateFormat("HH:mm", Locale.getDefault());
                                        var hourStartFilter: Date? = null
                                        var hourEndFilter: Date? = null
                                        var dateFilter: Date? = null
                                        if (hourStart != "hh:mm") {
                                            hourStartFilter = hourFormat.parse(hourStart)!!
                                        }
                                        if (hourEnd != "hh:mm") {
                                            hourEndFilter = hourFormat.parse(hourEnd)!!
                                        }
                                        if (dateResult != "dd/mm/yyyy") {
                                            dateFilter = dateFormat.parse(dateResult)!!
                                        }

                                        filterCategory.forEachIndexed { index, item ->
                                            if (item == 1) {
                                                categoryPick.add(category[index])
                                            }
                                            if (lokasi[index] == 1) {
                                                lokasiPick.add(
                                                    if (listLokasi[index].contains(" "))
                                                        listLokasi[index].replace(" ", "_")
                                                    else
                                                        listLokasi[index]
                                                )
                                            }
                                        }
                                        kategori = "";
                                        homeViewModel.fetchBarangFilter(
                                            categoryPick,
                                            lokasiPick,
                                            dateFilter,
                                            hourStartFilter,
                                            hourEndFilter
                                        )

                                        //kembalikan filter
                                        isFilter = false;
                                        dateResult = "dd/mm/yyyy";
                                        hourStart = "hh:mm";
                                        hourEnd = "hh:mm";
                                        filterCategory.forEachIndexed { index, _ ->
                                            filterCategory[index] = 0
                                        }
                                        lokasi.forEachIndexed { index, _ ->
                                            lokasi[index] = 0
                                        }
                                    }
                                },
                                shape = RoundedCornerShape(size = 50.dp),
                                elevation = ButtonDefaults.buttonElevation(4.dp),
                                colors = ButtonDefaults.buttonColors(
                                    Color(0xFFF98125)
                                ),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    text = "Tampilkan Barang",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontFamily = poppinsFamily,
                                        fontWeight = FontWeight(700),
                                        color = Color(0xFFFFFFFF),
                                        textAlign = TextAlign.Center,
                                    )
                                )
                            }
                        }
                    }
                }
            }
            if (calendarOpen) {
                CalendarDialog(
                    state = calendarState,
                    selection = CalendarSelection.Date { date ->
                        dateResult = "$date"
                        Log.d("date: ", "$date")
                    },
                    config = CalendarConfig(
                        monthSelection = true,
                        yearSelection = true
                    )
                )
            }
            if (hourStartOpen) {
                ClockDialog(
                    state = hourStartState,
                    selection = ClockSelection.HoursMinutes { hours, minutes ->
                        hourStart = "$hours:$minutes"
                    },
                    config = ClockConfig(
                        is24HourFormat = true
                    )
                )
            }
            if (hourEndOpen) {
                ClockDialog(
                    state = hourEndState,
                    selection = ClockSelection.HoursMinutes { hours, minutes ->
                        hourEnd = "$hours:$minutes"
                    },
                    config = ClockConfig(
                        is24HourFormat = true
                    )
                )
            }
            //dialog lapor
            if (openDialog) {
                Dialog(onDismissRequest = {
                    openDialog = false
                }) {
                    Box(
                        modifier = Modifier
                            .width(300.dp)
                            .height(350.dp)
                            .background(
                                color = Color(0xFFFFFFFF),
                                shape = RoundedCornerShape(size = 20.dp)
                            )
                            .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .zIndex(1f),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Image(
                                modifier = Modifier
                                    .padding(1.dp)
                                    .width(35.dp)
                                    .height(35.dp)
                                    .clickable {
                                        openDialog = false
                                    },
                                painter = painterResource(id = R.drawable.cancellaporan_icon),
                                contentDescription = "image description",
                                contentScale = ContentScale.None
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = Color(0xFFFFFFFF),
                                    shape = RoundedCornerShape(size = 20.dp)
                                )
                                .padding(top = 28.5.dp)
                                .zIndex(0f),
                            verticalArrangement = Arrangement.spacedBy(
                                12.dp,
                                Alignment.CenterVertically
                            ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Image(
                                modifier = Modifier
                                    .padding(1.dp)
                                    .width(75.dp)
                                    .height(75.dp),
                                painter = painterResource(id = R.drawable.laporbarang_icon),
                                contentDescription = "image description",
                                contentScale = ContentScale.None
                            )
                            Text(
                                text = "Menemukan Barang?",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = poppinsFamily,
                                    fontWeight = FontWeight(600),
                                    color = Color(0xFF000000),
                                    textAlign = TextAlign.Center,
                                )
                            )
                            Button(
                                modifier = Modifier
                                    .shadow(
                                        elevation = 16.100000381469727.dp,
                                        spotColor = Color(0x26000000),
                                        ambientColor = Color(0x26000000)
                                    )
                                    .width(220.dp)
                                    .height(35.dp),
                                onClick = {
                                    navController!!.navigate("lapor_screen/FOUND")
                                    openDialog = false
                                },
                                contentPadding = PaddingValues(0.dp),
                                shape = RoundedCornerShape(size = 7.dp),
                                colors = ButtonDefaults.buttonColors(Color(0xFFF98125))
                            ) {
                                Text(
                                    text = "Lapor Barang Ditemukan",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontFamily = poppinsFamily,
                                        fontWeight = FontWeight(700),
                                        color = Color(0xFFFFFFFF),
                                        textAlign = TextAlign.Center,
                                    )
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(15.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(2.dp)
                                        .background(Color(0x121E1E1E)),
                                )
                                Text(
                                    text = "Atau",
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        fontFamily = poppinsFamily,
                                        fontWeight = FontWeight(500),
                                        color = Color(0xFF6F6F6F),

                                        )
                                )
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(2.dp)
                                        .background(Color(0x121E1E1E)),
                                )
                            }
                            Text(
                                text = "Kehilangan Barang?",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = poppinsFamily,
                                    fontWeight = FontWeight(600),
                                    color = Color(0xFF000000),
                                    textAlign = TextAlign.Center,
                                )
                            )
                            Button(
                                modifier = Modifier
                                    .shadow(
                                        elevation = 16.100000381469727.dp,
                                        spotColor = Color(0x26000000),
                                        ambientColor = Color(0x26000000)
                                    )
                                    .width(220.dp)
                                    .height(35.dp),
                                onClick = {
                                    navController!!.navigate("lapor_screen/LOST")
                                    openDialog = false
                                },
                                contentPadding = PaddingValues(0.dp),
                                shape = RoundedCornerShape(size = 7.dp),
                                colors = ButtonDefaults.buttonColors(Color(0xFF2C599D))
                            ) {
                                Text(
                                    text = "Lapor Barang Hilang",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontFamily = poppinsFamily,
                                        fontWeight = FontWeight(700),
                                        color = Color(0xFFFFFFFF),
                                        textAlign = TextAlign.Center,
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}