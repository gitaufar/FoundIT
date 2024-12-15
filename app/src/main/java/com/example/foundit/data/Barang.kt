package com.example.foundit.data

import com.example.foundit.enumclass.Categories
import com.example.foundit.enumclass.Status
import com.example.foundit.enumclass.Tipe
import java.util.Date

class Barang(
    val id: Int,
    val kategori: Categories?,
    val tipe: Tipe?,
    val nama: String?,
    val deskripsi: String?,
    val tanggal: Date?,
    val lokasi: String?,
    val kontak: String?,
    val foto: String?,
    val status: Status?,
    val foto_binary: ByteArray?,
    val nama_pelapor: String? = "data tidak terambil"
) {

    fun tambahBarang(): Boolean {
        return true;
    }

    fun updateStatusBarang(): Unit {

    }

    fun verifikasiPengambilanBarang(): Boolean {
        return true;
    }

}
