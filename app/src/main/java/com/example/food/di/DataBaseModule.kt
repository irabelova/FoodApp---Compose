package com.example.food.di

import android.content.Context
import androidx.room.Room
import com.example.food.data.database.FoodDao
import com.example.food.data.database.FoodDataBase
import dagger.Module
import dagger.Provides

@Module
class DataBaseModule {

    @Provides
    fun getDataSource(context: Context): FoodDataBase{
        return Room.databaseBuilder(
            context.applicationContext,
            FoodDataBase::class.java,
            "food_database"
        )
            .fallbackToDestructiveMigration()
            .build()

    }

    @Provides
    fun provideDao(database: FoodDataBase): FoodDao {
        return database.foodDao()
    }
}