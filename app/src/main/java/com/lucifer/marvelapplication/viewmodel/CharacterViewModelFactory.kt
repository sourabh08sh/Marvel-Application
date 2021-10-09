package com.lucifer.marvelapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lucifer.marvelapplication.repository.MainRepository

// As we are using parameterized view model so we have to define factory for that view model. It creates object of view model.
class CharacterViewModelFactory(private val repository: MainRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CharacterViewModel(repository) as T
    }
}