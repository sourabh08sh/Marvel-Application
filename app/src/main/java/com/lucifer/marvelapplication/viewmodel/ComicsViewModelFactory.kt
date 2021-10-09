package com.lucifer.marvelapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lucifer.marvelapplication.repository.MainRepository

class ComicsViewModelFactory(private val repository: MainRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ComicsViewModel(repository) as T
    }
}