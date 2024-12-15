package com.example.foundit.ui.widget

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale

@Composable
fun ByteArrayImage(byteArray: ByteArray?, modifier: Modifier = Modifier) {
    // Konversi ByteArray menjadi Bitmap hanya sekali
    val bitmap = remember(byteArray) {
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
    }

    // Tampilkan gambar menggunakan BitmapPainter
    Image(
        painter = BitmapPainter(bitmap.asImageBitmap()),
        contentDescription = "Gambar dari ByteArray",
        modifier = modifier,
        contentScale = ContentScale.FillBounds
    )
}