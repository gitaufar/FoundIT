package com.example.foundit.ui.widget

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.foundit.R
import com.example.foundit.data.Barang
import com.example.foundit.data.Database
import com.example.foundit.enumclass.Categories
import com.example.foundit.enumclass.Tipe
import com.example.foundit.ui.theme.poppinsFamily
import com.example.foundit.ui.viewmodel.DetailedViewModel
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun Garis(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth()
            .height(1.dp)
            .background(color = Color(0x33000000))
    )
}


@Composable
fun DetailedItemScreen(
    modifier: Modifier = Modifier,
    navController: NavController? = null,
    id_barang: Int
) {
    val viewModel = viewModel<DetailedViewModel>()
    val barang by viewModel.barang.collectAsState()
    viewModel.fetchBarangId(id_barang)
    LaunchedEffect(Unit) {
        viewModel.fetchBarangId(id_barang)
    }
    val hariFormat = SimpleDateFormat("EEEE", Locale.getDefault())
    val bulanFormat = SimpleDateFormat("MMMM", Locale.getDefault())
    val tanggalFormat = SimpleDateFormat("dd", Locale.getDefault())
    val tahunFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    val greyOff = Color(0xFF6F6F6F)
    val categoryImage = mapOf(
        Categories.ELEKTRONIK to R.drawable.elektronik,
        Categories.BOTOL to R.drawable.botol,
        Categories.KUNCI to R.drawable.key,
        Categories.DOMPET to R.drawable.dompet,
        Categories.LAINNYA to R.drawable.lainnya,
        Categories.AKSESORIS to R.drawable.aksesoris,
        Categories.ALAT_TULIS to R.drawable.alat_tulis,
        Categories.PAKAIAN to R.drawable.pakaian,
        Categories.SMARTPHONE to R.drawable.smartphone
    )
    val scrollState = rememberScrollState()
    
    barang?.let { barang ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(scrollState),
        ) {
            val waktu = barang.tanggal
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(color = Color(0xFF193A6F)),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp, start = 21.dp, end = 26.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        modifier = Modifier
                            .padding(1.dp)
                            .width(30.dp)
                            .height(30.dp)
                            .clickable {
                                navController!!.navigate("home_screen")
                            },
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "image description",
                        contentScale = ContentScale.None
                    )
                    Spacer(modifier = Modifier.size(7.dp))
                    Text(
                        text = if(barang.tipe == Tipe.FOUND) "Detail Barang Temuan" else "Detail Barang Hilang",
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
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier
                                .padding(1.dp)
                                .width(24.dp)
                                .height(24.dp),
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = "image description",
                            contentScale = ContentScale.None
                        )
                    }
                }
            }
            if(barang.foto != null){
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(412.dp),
                    painter = rememberAsyncImagePainter(barang.foto),
                    contentDescription = "image description",
                    contentScale = ContentScale.FillBounds
                )
            } else {
                ByteArrayImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(412.dp),
                    byteArray = barang.foto_binary
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFFFFFFFF))
                    .padding(start = 26.dp, top = 20.dp, end = 26.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = barang.nama ?: "",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight(700),
                        color = Color(0xFF193A6F),
                    )
                )
                Box(
                    modifier = Modifier
                        .padding(0.dp)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(color = Color(0x33000000))
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
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(1.dp)
                            .width(22.dp)
                            .height(22.dp),
                        tint = Color(0xFF6F6F6F),
                        painter = painterResource(categoryImage[barang.kategori] ?: R.drawable.lainnya),
                        contentDescription = "image description",
                    )
                    Text(
                        text = barang.kategori.toString()[0] + barang.kategori.toString().drop(1)
                            .lowercase(),
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight(400),
                            color = Color(0xFF6F6F6F),
                        )
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(0.dp)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(color = Color(0x33000000))
                )
                Text(
                    text = "Lokasi Ditemukan",
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
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(1.dp)
                            .width(22.dp)
                            .height(22.dp),
                        painter = painterResource(id = R.drawable.location),
                        contentDescription = "image description",
                        tint = Color(0xFF6F6F6F)
                    )
                    Text(
                        text = barang.lokasi ?: "",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight(400),
                            color = Color(0xFF6F6F6F),
                        )
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(0.dp)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(color = Color(0x33000000))
                )
                Text(
                    text = "Waktu Ditemukan",
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
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(1.dp)
                            .width(22.dp)
                            .height(22.dp),
                        painter = painterResource(id = R.drawable.date),
                        contentDescription = "image description",
                        tint = Color(0xFF6F6F6F)
                    )
                    Text(
                        text = "${hariFormat.format(waktu)}, ${tanggalFormat.format(waktu)} ${
                            bulanFormat.format(
                                waktu
                            )
                        } ${tahunFormat.format(waktu)}",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight(400),
                            color = greyOff,
                        )
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(1.dp)
                            .width(22.dp)
                            .height(22.dp),
                        painter = painterResource(id = R.drawable.waktu_detail),
                        contentDescription = "image description",
                        tint = Color(0xFF6F6F6F)
                    )
                    Log.d("tanggal", "$waktu")
                    Text(
                        text = "Dilaporkan pada ${
                            SimpleDateFormat("HH:mm", Locale.getDefault()).format(
                                waktu
                            )
                        } WIB",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight(400),
                            color = greyOff,
                        )
                    )
                }
                Garis()
                Text(
                    text = "Deskripsi",
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
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(1.dp)
                            .width(22.dp)
                            .height(22.dp),
                        tint = greyOff,
                        painter = painterResource(id = R.drawable.deskripsi_icon),
                        contentDescription = "image description",
                    )
                    Text(
                        text = barang.deskripsi ?: "",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight(400),
                            color = greyOff,
                        )
                    )
                }
                Garis()
                Text(
                    text = "Kontak Pelapor",
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
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(1.dp)
                            .width(24.dp)
                            .height(24.dp),
                        painter = painterResource(id = R.drawable.contact),
                        contentDescription = "image description",
                    )
                    Text(
                        text = barang.nama_pelapor ?: "data tidak terambil",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight(400),
                            color = Color(0xFF6F6F6F),
                        )
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(1.dp)
                            .width(24.dp)
                            .height(24.dp),
                        painter = painterResource(id = R.drawable.no_hp),
                        contentDescription = "image description",
                    )
                    var no_hp = ""
                    barang.kontak!!.forEachIndexed { index, item ->
                        if(index != 4 && index != 8) no_hp += item else no_hp += "-$item"
                    }
                    Text(
                        text = no_hp,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight(400),
                            color = Color(0xFF6F6F6F),
                        )
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(start = 20.dp, end = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Button(
                        modifier = Modifier
                            .weight(9f),
                        onClick = {

                        },
                        shape = RoundedCornerShape(size = 6.dp),
                        colors = ButtonDefaults.buttonColors(
                            Color(0xFF2C599D)
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            4.dp
                        )
                    ){
                        Text(
                            text = "Hubungi Pelapor",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = poppinsFamily,
                                fontWeight = FontWeight(700),
                                color = Color(0xFFFFFFFF),
                                textAlign = TextAlign.Center,
                            )
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .shadow(elevation = 10.dp, spotColor = Color(0x33000000), ambientColor = Color(0x33000000))
                            .width(35.dp)
                            .height(35.dp)
                            .background(color = Color(0xFFF98125), shape = RoundedCornerShape(size = 6.dp))
                            .padding(start = 5.5.dp, top = 5.70056.dp, end = 5.5.dp, bottom = 5.29944.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            tint = Color.White,
                            modifier = Modifier
                                .padding(1.dp)
                                .width(24.dp)
                                .height(24.dp),
                            painter = painterResource(id = R.drawable.setting_putih),
                            contentDescription = "image description",
                        )
                    }
                }

            }
        }
    }
}