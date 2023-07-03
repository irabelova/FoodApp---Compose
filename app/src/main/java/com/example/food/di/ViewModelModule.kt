package com.example.food.di

import androidx.lifecycle.ViewModel
import com.example.food.presentation.FoodViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(FoodViewModel::class)
    abstract fun bindFoodViewModel(viewModel: FoodViewModel): ViewModel
}