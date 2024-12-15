package com.example.foundit.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foundit.data.Barang
import com.example.foundit.data.Database
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailedViewModel: ViewModel() {
    private val _barang = MutableStateFlow<Barang?>(null)
    val barang: StateFlow<Barang?> = _barang

    fun fetchBarangId(id: Int){
        viewModelScope.launch {
            _barang.value = Database().getBarangDataId(id)
        }
    }
}