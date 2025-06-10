package com.evermore.beholder.di

import android.content.Context
import com.evermore.beholder.presentation.viewmodels.ClassDetailsViewModel
import com.evermore.beholder.presentation.viewmodels.RaceDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ClassDetailsViewModel() }
    viewModel {
        RaceDetailsViewModel { stringResId, arg ->
            val context: Context = get()
            if (arg != null) {
                context.getString(stringResId, arg)
            } else {
                context.getString(stringResId)
            }
        }
    }
}