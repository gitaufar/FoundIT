package com.example.foundit.data

import com.example.foundit.enumclass.Status
import com.example.foundit.enumclass.Tipe
import java.util.Date

class Laporan(
    val barang: String,
    val tipe_laporan: Tipe,
    val status_laporan: Status,
    val tanggal_laporan : Date,
    val tanggal_update: Date
    ) {
}