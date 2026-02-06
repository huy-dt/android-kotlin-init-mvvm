package com.huydt.uikit.list.viewmodel

import com.huydt.uikit.list.data.ListRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ListViewModelFactory<T>(
    private val repository: ListRepository<T>
) : ViewModelProvider.Factory {

    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
        return ListViewModel(repository) as VM
    }
}
