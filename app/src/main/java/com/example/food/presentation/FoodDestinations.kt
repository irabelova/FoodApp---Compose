package com.example.food.presentation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.food.R

const val FOOD_ITEM_ID = "FOOD_ITEM_ID"

sealed class FoodBottomMenuItem(
    val imageVector: ImageVector,
    @StringRes val text: Int,
    val route: String,
) {
    object Menu : FoodBottomMenuItem(
        imageVector = Icons.Filled.Fastfood,
        text = R.string.menu,
        route = "menu"
    )

    object Profile : FoodBottomMenuItem(
        imageVector = Icons.Filled.Person,
        text = R.string.profile,
        route = "profile"
    )

    object ShoppingCart : FoodBottomMenuItem(
        imageVector = Icons.Filled.ShoppingBasket,
        text = R.string.shopping_cart,
        route = "shoppingCart"
    )

    object Cities: FoodBottomMenuItem(
        imageVector = Icons.Filled.Fastfood,
        text = R.string.menu,
        route = "cities"
    )

    object Banners: FoodBottomMenuItem(
        imageVector = Icons.Filled.Fastfood,
        text = R.string.menu,
        route = "banners/{steps}/{currentStep}"
    )

    object FoodItem: FoodBottomMenuItem(
        imageVector = Icons.Filled.Fastfood,
        text = R.string.menu,
        route = "foodItem/{$FOOD_ITEM_ID}"
    )
}