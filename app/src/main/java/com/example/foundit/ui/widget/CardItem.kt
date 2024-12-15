package com.example.foundit.ui.widget

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.foundit.R
import com.example.foundit.data.Barang
import com.example.foundit.enumclass.Categories
import com.example.foundit.enumclass.Status
import com.example.foundit.enumclass.Tipe
import com.example.foundit.ui.theme.poppinsFamily
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
val customDateTime = LocalDateTime.of(2024, 11, 20, 9, 30)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CardItem(
    modifier: Modifier = Modifier,
    barang: Barang = Barang(
        id = 1,
        status = Status.IN_PROGRESS,
        kategori = Categories.ELEKTRONIK,
        tipe = Tipe.LOST,
        nama = "Laptop ASUS",
        deskripsi = "Laptop ASUS yang hilang di kampus",
        tanggal = Date.from(customDateTime.atZone(ZoneId.systemDefault()).toInstant()),
        lokasi = "Gedung F, Fakultas Ilmu Komputer",
        kontak = "08123456789",
        foto = "https://media.karousell.com/media/photos/products/2019/08/16/laptop_asus_1565945380_f189aa97_progressive.jpg",
        foto_binary = null
    ),
    onItemClick: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(5.dp)
    ) {

        Column(
            modifier = Modifier
                .shadow(
                    elevation = 17.dp,
                    spotColor = Color(0x33000000),
                    ambientColor = Color(0x33000000)
                )
                .fillMaxWidth()
                .height(250.dp)
                .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 15.dp))
                .clickable {
                    onItemClick()
                    Log.d("CardItem","Card Item di klik")
                },
        ) {
            if(barang.foto != null){
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp),
                    painter = rememberImagePainter(barang.foto),
                    contentDescription = "image description",
                    contentScale = ContentScale.FillBounds
                )
            } else {
                ByteArrayImage(
                    byteArray = barang.foto_binary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = barang.nama ?: "",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight(600),
                        color = Color(0xFF193A6F),
                    )
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        modifier = Modifier
                            .padding(1.dp)
                            .width(12.dp)
                            .height(12.dp),
                        painter = painterResource(id = R.drawable.location),
                        contentDescription = "image description",
                        contentScale = ContentScale.None
                    )
                    Row(
                        modifier = Modifier
                            .width(133.dp)
                            .height(15.dp)
                            .horizontalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = barang.lokasi ?: "",
                            style = TextStyle(
                                fontSize = 10.sp,
                                fontFamily = poppinsFamily,
                                fontWeight = FontWeight(400),
                                color = Color(0xFF6F6F6F),
                            )
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(7.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        modifier = Modifier
                            .padding(1.dp)
                            .width(12.dp)
                            .height(12.dp),
                        painter = painterResource(id = R.drawable.waktu),
                        contentDescription = "image description",
                        contentScale = ContentScale.None
                    )

                    Row(
                        modifier = Modifier
                            .width(133.dp)
                            .height(15.dp)
                            .horizontalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = getWaktu(barang.tanggal ?: Date()),
                            style = TextStyle(
                                fontSize = 10.sp,
                                fontFamily = poppinsFamily,
                                fontWeight = FontWeight(400),
                                color = Color(0xFF6F6F6F),
                            )
                        )
                    }
                }

            }
        }
    }
}

fun getWaktu(date: Date): String {

    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    //item
    val formatItem = dateFormat.format(date)
    val lostTime = formatItem.split(Regex("[- :]+"))
    //now
    val formatDate = dateFormat.format(Date())
    val nowTime = formatDate.split(Regex("[- :]+"))

    val selisih_tahun = Math.abs(nowTime[0].toInt() - lostTime[0].toInt())
    val selisih_bulan = Math.abs(nowTime[1].toInt() - lostTime[1].toInt())
    val selisih_hari = Math.abs(nowTime[2].toInt() - lostTime[2].toInt())
    val selisih_jam = Math.abs(nowTime[3].toInt() - lostTime[3].toInt())
    val selisih_menit = Math.abs(nowTime[4].toInt() - lostTime[4].toInt())

    if(selisih_tahun > 0){
        return "$selisih_tahun tahun yang lalu"
    }

    if(selisih_bulan > 0){
        return "$selisih_bulan bulan yang lalu"
    }

    if(selisih_hari > 0){
        return "$selisih_hari hari yang lalu"
    }

    if(selisih_jam > 0){
        return "$selisih_jam jam yang lalu"
    }
    return "$selisih_menit menit yang lalu"
}