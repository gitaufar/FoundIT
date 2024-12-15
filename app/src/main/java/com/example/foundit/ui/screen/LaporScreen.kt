package com.example.foundit.ui.screen

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.navigation.NavController
import com.example.foundit.R
import com.example.foundit.data.Barang
import com.example.foundit.data.Database
import com.example.foundit.data.Laporan
import com.example.foundit.data.User
import com.example.foundit.enumclass.Categories
import com.example.foundit.enumclass.Status
import com.example.foundit.enumclass.Tipe
import com.example.foundit.ui.theme.poppinsFamily
import com.example.foundit.ui.viewmodel.HomeViewModel
import com.example.foundit.ui.widget.ByteArrayImage
import com.example.foundit.ui.widget.LaporTextField
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun LaporScreen(
    modifier: Modifier = Modifier,
    tipe_barang: Tipe? = null,
    navController: NavController? = null,
    homeViewModel: HomeViewModel? = null
) {
    val deskripsiState = remember { TextFieldState("") }
    val namaBarangState = remember { TextFieldState("") }
    val namaTempatState = remember { TextFieldState("") }
    val alamatLengkapState = remember { TextFieldState("") }
    val namaPelaporState = remember { TextFieldState(User.userName ?: "") }
    val noTelpState = remember { TextFieldState(User.nomor_telepon ?: "") }
    var pickCategory by remember { mutableStateOf(false) }
    var pickDate by remember { mutableStateOf(false) }
    var pickHour by remember { mutableStateOf(false) }
    var pickLokasi by remember { mutableStateOf(false) }
    var showAlert by remember { mutableStateOf(false) }
    var showConfirm by remember { mutableStateOf(false) }
    var showImage by remember { mutableStateOf(false) }
    var tipe by remember { mutableStateOf(tipe_barang) }
    var category by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("dd/mm/yyyy") }
    var hour by remember { mutableStateOf("hh:mm") }
    var byteArray: ByteArray? by remember { mutableStateOf(null) }
    val scrollState = rememberScrollState()
    val calendarState = rememberSheetState()
    val hourState = rememberSheetState()
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        //layer atas start
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color(0xFF193A6F))
                .padding(bottom = 16.dp, start = 21.dp, end = 26.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .padding(1.dp)
                        .width(30.dp)
                        .height(30.dp)
                        .clickable { navController!!.navigate("home_screen") },
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "image description",
                    contentScale = ContentScale.FillBounds
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = if (tipe == Tipe.FOUND) "Lapor Barang Ditemukan" else "Lapor Barang Hilang",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight(600),
                        color = Color(0xFFFFFFFF),
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .width(44.dp)
                        .height(44.dp)
                        .background(
                            color = Color(0xFFFFFFFF),
                            shape = RoundedCornerShape(size = 10.dp)
                        )
                        .clickable {
                            if (tipe == Tipe.FOUND) tipe = Tipe.LOST else tipe = Tipe.FOUND
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier
                            .padding(1.dp)
                            .width(24.dp)
                            .height(24.dp),
                        painter = painterResource(id = R.drawable.switch_icon),
                        contentDescription = "image description",
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }
        //layer atas end

        //layer tengah start
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 26.dp, top = 10.dp, end = 26.dp, bottom = 40.dp)
                .verticalScroll(state = scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = "Nama Barang",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight(600),
                    color = Color(0xFF000000),
                )
            )
            LaporTextField(
                state = namaBarangState,
                hint = "Masukkan nama barang"
            )
            Text(
                text = "Kategori",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight(600),
                    color = Color(0xFF000000),
                )
            )
            Row(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color(0xFFB0B0B0),
                        shape = RoundedCornerShape(size = 50.dp)
                    )
                    .fillMaxWidth()
                    .height(32.dp)
                    .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 50.dp))
                    .padding(horizontal = 12.dp)
                    .clickable {
                        pickCategory = !pickCategory
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = if (category.isEmpty()) "Pilih Kategori" else category,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight(400),
                        color = if (category.isEmpty()) Color(0xFFB0B0B0) else Color.Black,
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(id = R.drawable.dropdown_icon),
                    contentDescription = "image description",
                    contentScale = ContentScale.None
                )
            }
            if (pickCategory) {
                //sedang memilih category
                LazyColumn(
                    modifier = Modifier
                        .drawBehind {
                            val borderWidth = 1.dp.toPx()
                            val cornerRadius = 12.dp.toPx()

                            // Gambar border hanya di kiri, kanan, dan bawah dengan lengkungan
                            drawRoundRect(
                                color = Color(0xFFB0B0B0),
                                size = Size(size.width, size.height), // Ukuran total
                                style = Stroke(width = borderWidth), // Ketebalan garis
                                cornerRadius = CornerRadius(cornerRadius, cornerRadius) // Radius lengkungan
                            )
                        }
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val listCategory = Categories.entries.toTypedArray()
                    items(listCategory.size) {
                        val kategori = listCategory[it].name
                        Text(
                            modifier = Modifier
                                .clickable{
                                    category = kategori
                                    pickCategory = false
                                }
                            ,
                            text = kategori,
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = Color.Black,
                                fontFamily = poppinsFamily,
                            )
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Lokasi",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight(600),
                        color = Color(0xFF000000),
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Kota Malang",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF6F6F6F),
                    )
                )
                Image(
                    modifier = Modifier.graphicsLayer(
                        rotationZ = if (pickLokasi) 0f else -90f // Rotasi 90 derajat
                    ),
                    painter = painterResource(id = R.drawable.dropdown_icon),
                    contentDescription = "image description",
                    contentScale = ContentScale.None
                )
            }
            if (pickLokasi) {
                //sedang memilih lokasi
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    modifier = Modifier
                        .padding(1.dp)
                        .width(22.dp)
                        .height(22.dp),
                    painter = painterResource(id = R.drawable.nama_tempat_icon),
                    contentDescription = "image description",
                    contentScale = ContentScale.FillBounds
                )
                Text(
                    text = "Nama Tempat",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF6F6F6F),
                    )
                )
            }
            LaporTextField(
                state = namaTempatState,
                hint = "Masukkan nama tempat"
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    modifier = Modifier
                        .padding(1.dp)
                        .width(22.dp)
                        .height(22.dp),
                    painter = painterResource(id = R.drawable.alamat_lengkap_icon),
                    contentDescription = "image description",
                    contentScale = ContentScale.FillBounds
                )
                Text(
                    text = "Alamat Lengkap",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF6F6F6F),
                    )
                )
            }
            LaporTextField(
                state = alamatLengkapState,
                hint = "Masukkan alamat lengkap"
            )
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
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier
                        .padding(1.dp)
                        .width(22.dp)
                        .height(22.dp),
                    painter = painterResource(id = R.drawable.date),
                    contentDescription = "image description",
                    contentScale = ContentScale.None
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Tanggal Ditemukan",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF6F6F6F),
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color(0xFFB0B0B0),
                            shape = RoundedCornerShape(size = 50.dp)
                        )
                        .width(160.dp)
                        .height(32.dp)
                        .background(
                            color = Color(0xFFFFFFFF),
                            shape = RoundedCornerShape(size = 50.dp)
                        )
                        .padding(start = 15.dp, end = 15.dp)
                        .clickable {
                            pickDate = true
                            calendarState.show()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = date,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight(400),
                            color = if(date == "dd/mm/yyyy") Color(0xFFB0B0B0) else Color.Black,
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        modifier = Modifier
                            .padding(1.dp)
                            .width(15.dp)
                            .height(30.dp),
                        painter = painterResource(id = R.drawable.dropdown_icon),
                        contentDescription = "image description",
                        contentScale = ContentScale.None
                    )
                }
            }
            if(pickDate){
                CalendarDialog(
                    state = calendarState,
                    selection = CalendarSelection.Date { dateResult ->
                        date = "$dateResult"
                        Log.d("date: ", "$dateResult")
                    },
                    config = CalendarConfig(
                        monthSelection = true,
                        yearSelection = true
                    )
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier
                        .padding(1.dp)
                        .width(22.dp)
                        .height(22.dp),
                    painter = painterResource(id = R.drawable.waktu_detail),
                    contentDescription = "image description",
                    contentScale = ContentScale.None
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Jam Ditemukan",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF6F6F6F),
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color(0xFFB0B0B0),
                            shape = RoundedCornerShape(size = 50.dp)
                        )
                        .width(160.dp)
                        .height(32.dp)
                        .background(
                            color = Color(0xFFFFFFFF),
                            shape = RoundedCornerShape(size = 50.dp)
                        )
                        .padding(start = 15.dp, end = 15.dp)
                        .clickable {
                            pickHour = true
                            hourState.show()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = hour,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight(400),
                            color = if(hour == "hh:mm") Color(0xFFB0B0B0) else Color.Black,
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        modifier = Modifier
                            .padding(1.dp)
                            .width(15.dp)
                            .height(30.dp),
                        painter = painterResource(id = R.drawable.dropdown_icon),
                        contentDescription = "image description",
                        contentScale = ContentScale.None
                    )
                }
            }
            if(pickHour){
                ClockDialog(
                    state = hourState,
                    selection = ClockSelection.HoursMinutes { hours, minutes ->
                        hour = "$hours:$minutes"
                    },
                    config = ClockConfig(
                        is24HourFormat = true
                    )
                )
            }
            Text(
                text = "Deskripsi",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight(600),
                    color = Color(0xFF000000),
                )
            )
            BasicTextField(
                state = deskripsiState,
                decorator = { innerText ->
                    Row(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = Color(0xFFB0B0B0),
                                shape = RoundedCornerShape(size = 15.dp)
                            )
                            .fillMaxWidth()
                            .height(110.dp)
                            .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            Image(
                                modifier = Modifier
                                    .padding(1.dp)
                                    .width(22.dp)
                                    .height(35.dp),
                                painter = painterResource(id = R.drawable.deskripsi_icon),
                                contentDescription = "image description",
                                contentScale = ContentScale.None
                            )
                            Box {
                                if (deskripsiState.text.isEmpty()) {
                                    Text(
                                        text = "Masukkan deskripsi\nex: ciri-ciri, warna, merek, model, bahan, kronologi\n",
                                        style = TextStyle(
                                            fontSize = 12.sp,
                                            fontFamily = poppinsFamily,
                                            fontWeight = FontWeight(400),
                                            color = Color(0xFFB0B0B0),
                                        )
                                    )
                                }
                                innerText()
                            }
                        }
                    }
                }
            )
            Text(
                text = "Kontak",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight(600),
                    color = Color(0xFF000000),
                )
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    modifier = Modifier
                        .padding(1.dp)
                        .width(22.dp)
                        .height(22.dp),
                    painter = painterResource(id = R.drawable.contact),
                    contentDescription = "image description",
                    contentScale = ContentScale.FillBounds
                )
                Text(
                    text = "Nama Pelapor",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF6F6F6F),
                    )
                )
            }
            LaporTextField(
                state = namaPelaporState,
                hint = "Masukkan nama pelapor"
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    modifier = Modifier
                        .padding(1.dp)
                        .width(22.dp)
                        .height(22.dp),
                    painter = painterResource(id = R.drawable.no_hp),
                    contentDescription = "image description",
                    contentScale = ContentScale.FillBounds
                )
                Text(
                    text = "Nomor Telepon",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF6F6F6F),
                    )
                )
            }
            LaporTextField(
                state = noTelpState,
                hint = "Masukkan nomor telepon"
            )
            Text(
                text = "Foto Barang",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight(600),
                    color = Color(0xFF000000),
                )
            )
            val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let { imageUri ->
                    val inputStream = context.contentResolver.openInputStream(imageUri)
                    byteArray = inputStream?.readBytes()
                    showImage = true
                }
            }

            if(showImage){
                ByteArrayImage(
                    byteArray = byteArray,
                    modifier = Modifier
                        .width(360.dp)
                        .height(270.dp)
                        .clip(
                            RoundedCornerShape(15.dp)
                        )
                )
            }

            if(!showImage){
                Column(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color(0xFFB0B0B0),
                            shape = RoundedCornerShape(size = 15.dp)
                        )
                        .width(360.dp)
                        .height(110.dp)
                        .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
                        .clickable{
                            launcher.launch("image/*")
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier
                            .padding(0.dp)
                            .width(59.dp)
                            .height(56.74927.dp),
                        painter = painterResource(id = R.drawable.upload_icon),
                        contentDescription = "image description",
                        contentScale = ContentScale.FillBounds
                    )
                    Text(
                        text = "Upload foto barang di sini",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight(400),
                            color = Color(0xFFB0B0B0),
                        )
                    )
                }
            }


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    modifier = Modifier
                        .shadow(
                            elevation = 10.dp,
                            spotColor = Color(0x33000000),
                            ambientColor = Color(0x33000000)
                        )
                        .fillMaxWidth()
                        .height(35.dp),
                    onClick = {
                        showConfirm = true
                    },
                    shape = RoundedCornerShape(size = 6.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFFF98125)),
                    elevation = ButtonDefaults.buttonElevation(10.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Kirim Laporan",
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
            if(showAlert){
                Dialog(
                    onDismissRequest = {
                        showAlert = false
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .width(300.dp)
                            .height(350.dp)
                            .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 20.dp))
                            .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .zIndex(1f),
                            horizontalArrangement = Arrangement.End
                        ){
                            Image(
                                modifier = Modifier
                                    .padding(1.dp)
                                    .width(35.dp)
                                    .height(35.dp)
                                    .clickable{
                                        showAlert = false
                                    },
                                painter = painterResource(id = R.drawable.cancellaporan_icon),
                                contentDescription = "image description",
                                contentScale = ContentScale.None
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 20.dp))
                                .padding(top = 28.5.dp)
                                .zIndex(0f),
                            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = "Data belum lengkap...",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = poppinsFamily,
                                    fontWeight = FontWeight(600),
                                    color = Color(0xFF000000),
                                    textAlign = TextAlign.Center,
                                )
                            )
                            Text(
                                text = "Lengkapi semua form yang dibutuhkan",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = poppinsFamily,
                                    fontWeight = FontWeight(400),
                                    color = Color(0xFF6F6F6F),
                                    textAlign = TextAlign.Center,
                                )
                            )
                            Image(
                                modifier = Modifier
                                    .width(175.dp)
                                    .height(175.dp),
                                painter = painterResource(id = R.drawable.alert_ilustration),
                                contentDescription = "image description",
                                contentScale = ContentScale.FillBounds
                            )
                            Box(
                                modifier = Modifier
                                    .width(220.dp)
                                    .height(60.dp)
                                    .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp),
                                contentAlignment = Alignment.Center
                            ){
                                Button(
                                    modifier = Modifier
                                        .shadow(elevation = 16.100000381469727.dp, spotColor = Color(0x26000000), ambientColor = Color(0x26000000))
                                        .width(200.dp)
                                        .height(30.dp),
                                    onClick = {
                                        showAlert = false
                                    },
                                    contentPadding = PaddingValues(0.dp),
                                    shape = RoundedCornerShape(size = 7.dp),
                                    colors = ButtonDefaults.buttonColors(Color(0xFFF98125))
                                ) {
                                    Text(
                                        text = "Kembali",
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
            if(showConfirm){
                Dialog(
                    onDismissRequest = {
                        showConfirm = false
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .width(300.dp)
                            .height(350.dp)
                            .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 20.dp))
                            .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .zIndex(1f),
                            horizontalArrangement = Arrangement.End
                        ){
                            Image(
                                modifier = Modifier
                                    .padding(1.dp)
                                    .width(35.dp)
                                    .height(35.dp)
                                    .clickable{
                                        showConfirm = false
                                    },
                                painter = painterResource(id = R.drawable.cancellaporan_icon),
                                contentDescription = "image description",
                                contentScale = ContentScale.None
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 20.dp))
                                .padding(top = 28.5.dp)
                                .zIndex(0f),
                            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = "Kirim Laporan?",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = poppinsFamily,
                                    fontWeight = FontWeight(600),
                                    color = Color(0xFF000000),
                                    textAlign = TextAlign.Center,
                                )
                            )
                            Text(
                                text = "Pastikan semua data sudah valid",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = poppinsFamily,
                                    fontWeight = FontWeight(400),
                                    color = Color(0xFF6F6F6F),
                                    textAlign = TextAlign.Center,
                                )
                            )
                            Image(
                                modifier = Modifier
                                    .width(175.dp)
                                    .height(175.dp),
                                painter = painterResource(id = R.drawable.confirm_ilustration),
                                contentDescription = "image description",
                                contentScale = ContentScale.FillBounds
                            )
                            Row(
                                modifier = Modifier
                                    .width(240.dp)
                                    .height(60.dp)
                                    .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ){
                                Button(
                                    modifier = Modifier
                                        .shadow(elevation = 16.100000381469727.dp, spotColor = Color(0x26000000), ambientColor = Color(0x26000000))
                                        .weight(1f)
                                        .height(30.dp),
                                    onClick = {
                                        if(
                                            namaBarangState.text.isEmpty() ||
                                            category.isEmpty() ||
                                            namaTempatState.text.isEmpty() ||
                                            alamatLengkapState.text.isEmpty() ||
                                            date == "dd/mm/yyyy" ||
                                            hour == "hh:mm" ||
                                            deskripsiState.text.isEmpty() ||
                                            namaPelaporState.text.isEmpty() ||
                                            noTelpState.text.isEmpty() || !showImage
                                        ){
                                            showAlert = true
                                            showConfirm = false
                                        } else {
                                            coroutineScope.launch{
                                                val tanggal_kirim = parseStringToDate("$date $hour")
                                                val laporan = Laporan(
                                                    barang = namaBarangState.text.toString(),
                                                    tipe_laporan = tipe!!,
                                                    status_laporan = Status.IN_PROGRESS,
                                                    tanggal_laporan = Date(),
                                                    tanggal_update = Date()
                                                )
                                                val barang = Barang(
                                                    id = (Math.random() * 10).toInt(),
                                                    kategori = Categories.valueOf(category),
                                                    tipe = tipe,
                                                    nama = namaBarangState.text.toString(),
                                                    deskripsi = deskripsiState.text.toString(),
                                                    nama_pelapor = namaPelaporState.text.toString(),
                                                    tanggal = tanggal_kirim,
                                                    lokasi = namaTempatState.text.toString(),
                                                    kontak = noTelpState.text.toString(),
                                                    foto = null,
                                                    status = Status.IN_PROGRESS,
                                                    foto_binary = byteArray
                                                )
                                                val insertResult = Database().insertLaporan(laporan,barang,"$date $hour")
                                                if(insertResult){
                                                    homeViewModel!!.initBarangTemuan()
                                                    navController!!.navigate("home_screen")
                                                } else {
                                                    Toast.makeText(context,"gagal memasukkan laporan ke database coba lagi", Toast.LENGTH_LONG).show()
                                                    showConfirm = false
                                                }
                                            }
                                        }
                                    },
                                    contentPadding = PaddingValues(0.dp),
                                    shape = RoundedCornerShape(size = 7.dp),
                                    colors = ButtonDefaults.buttonColors(Color(0xFF2C599D))
                                ) {
                                    Text(
                                        text = "Konfirmasi",
                                        style = TextStyle(
                                            fontSize = 14.sp,
                                            fontFamily = poppinsFamily,
                                            fontWeight = FontWeight(700),
                                            color = Color(0xFFFFFFFF),
                                            textAlign = TextAlign.Center,
                                        )
                                    )
                                }

                                Button(
                                    modifier = Modifier
                                        .shadow(elevation = 16.100000381469727.dp, spotColor = Color(0x26000000), ambientColor = Color(0x26000000))
                                        .weight(1f)
                                        .height(30.dp),
                                    onClick = {
                                        showConfirm = false
                                    },
                                    contentPadding = PaddingValues(0.dp),
                                    shape = RoundedCornerShape(size = 7.dp),
                                    colors = ButtonDefaults.buttonColors(Color(0xFFF98125))
                                ) {
                                    Text(
                                        text = "Batal",
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
        //layer tengah end
    }
}

fun parseStringToDate(dateString: String): Date? {
    // Tentukan format tanggal yang sesuai dengan input
    val format = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
    return try {
        // Parse string menjadi objek Date
        format.parse(dateString)
    } catch (e: Exception) {
        e.printStackTrace() // Cetak error jika parsing gagal
        null
    }
}