package com.example.foundit.ui.viewmodel

import android.util.Log
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foundit.data.Barang
import com.example.foundit.data.Database
import com.example.foundit.enumclass.Categories
import com.example.foundit.enumclass.Tipe
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Date

class HomeViewModel : ViewModel() {
    private val _listBarangHilang = MutableStateFlow<List<Barang>>(emptyList())
    private val _listBarangTemuan = MutableStateFlow<List<Barang>>(emptyList())
    private val _listBarang = MutableStateFlow<List<Barang>>(emptyList())
    private val _temuan = MutableStateFlow<Boolean>(true)
    private val _hilang = MutableStateFlow<Boolean>(false)
    val listBarang: StateFlow<List<Barang>> = _listBarang
    val temuan: StateFlow<Boolean> = _temuan
    val hilang: StateFlow<Boolean> = _hilang

    suspend fun initBarangHilang(){
        _listBarangHilang.value = Database().getBarangHilangData()
    }

    suspend fun initBarangTemuan(){
        _listBarangTemuan.value = Database().getBarangTemuanData()
    }

    fun temuan() {
        _temuan.value = true
        _hilang.value = false
    }

    fun hilang() {
        _temuan.value = false
        _hilang.value = true
    }

    fun fetchBarangHilang() {
        viewModelScope.launch {
            if(_listBarangHilang.value.isEmpty()){
                _listBarang.value = Database().getBarangHilangData()
                _listBarangHilang.value = _listBarang.value
            } else {
                _listBarang.value = _listBarangHilang.value
            }
        }
    }

    fun fetchBarangTemuan() {
        viewModelScope.launch {
            if(_listBarangTemuan.value.isEmpty()){
                _listBarang.value = Database().getBarangTemuanData()
                _listBarangTemuan.value = _listBarang.value
            } else {
                _listBarang.value = _listBarangTemuan.value
            }
        }
    }

    fun fetchBarangCategory(category: String) {
        viewModelScope.launch {
            if(category.contains(" ")){
                category.replace(" ","_")
            }
            if(_hilang.value){
                _listBarang.value = _listBarangHilang.value.filter { it.kategori == Categories.valueOf(category) && it.tipe == Tipe.LOST}
            } else {
                _listBarang.value = _listBarangTemuan.value.filter { it.kategori == Categories.valueOf(category) && it.tipe == Tipe.FOUND}
            }

        }
    }

    fun fetchBarangSearch(hint: String) {
        viewModelScope.launch {
            val filterType =
                if (temuan.value) _listBarangTemuan.value else _listBarangHilang.value
            val filterHint =
                filterType.filter { it.nama!!.toLowerCase().contains(hint.toLowerCase().trim()) }
            _listBarang.value = filterHint
            Log.d("cari", "cari barang")
        }
    }

    fun fetchBarangFilter(
        category: ArrayList<String>,
        lokasi: ArrayList<String>,
        tanggal: Date?,
        waktuMulai: Date?,
        waktuSelesai: Date?
    ) {
        if (_listBarang.value.isEmpty()) {
            return
        }
        if (category.isNotEmpty()) {
            val categoryLowerCase = category.map { it.lowercase() }
            _listBarang.value = _listBarang.value.filter {
                categoryLowerCase.contains(
                    it.kategori.toString().lowercase()
                )
            }
        }

        if (lokasi.isNotEmpty()) {
            val lokasiLowerCase = lokasi.map { it.lowercase() }
            _listBarang.value =
                _listBarang.value.filter { lokasiLowerCase.contains(it.lokasi!!.lowercase()) }
        }

        if (tanggal != null) {
            _listBarang.value = _listBarang.value.filter {
                it.tanggal!!.year == tanggal.year && it.tanggal!!.month == tanggal.month && it.tanggal!!.day == tanggal.day
            }
        }

        if (waktuMulai != null) {
            _listBarang.value = _listBarang.value.filter {
                it.tanggal!!.hours >= waktuMulai.hours
            }
        }

        if (waktuSelesai != null) {
            _listBarang.value = _listBarang.value.filter {
                it.tanggal!!.hours <= waktuSelesai.hours
            }
        }

        Log.d("filter", "filter barang")
    }

}