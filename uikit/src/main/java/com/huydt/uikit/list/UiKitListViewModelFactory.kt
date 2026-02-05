package com.huydt.uikit.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UiKitListViewModelFactory<T>(
    private val repository: UiKitListRepository<T>
) : ViewModelProvider.Factory {

    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
        return UiKitListViewModel(repository) as VM
    }
}
