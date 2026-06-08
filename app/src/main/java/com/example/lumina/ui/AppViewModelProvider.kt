package com.example.lumina.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lumina.LuminaApplication
import com.example.lumina.ui.entry.EntryViewModel
import com.example.lumina.ui.home.HomeViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Lumina app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(luminaApplication().container.journalRepository)
        }
        // Initializer for EntryViewModel
        initializer {
            EntryViewModel(luminaApplication().container.journalRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [LuminaApplication].
 */
fun CreationExtras.luminaApplication(): LuminaApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as LuminaApplication)
