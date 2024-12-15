package com.example.foundit.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class BotNavViewModel : ViewModel() {
    var selectedItem by mutableStateOf(0)
        private set

    fun updateSelectedItem(index: Int) {
        selectedItem = index
    }
}