package com.example.food

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.food.di.AppComponent
import com.example.food.di.DaggerAppComponent
import com.example.food.di.ViewModelProviderFactory
import com.example.food.di.assistedViewModel
import com.example.food.presentation.ErrorScreen
import com.example.food.presentation.FOOD_ITEM_ID
import com.example.food.presentation.FoodBottomMenuItem
import com.example.food.presentation.FoodBottomNavigationMenu
import com.example.food.presentation.LoadingScreen
import com.example.food.presentation.bannerScreens.BannerScreen
import com.example.food.presentation.bannerScreens.BannerViewModel
import com.example.food.presentation.checkoutScreen.CheckoutScreen
import com.example.food.presentation.checkoutScreen.CheckoutViewModel
import com.example.food.presentation.foodItemScreen.FoodItemScreen
import com.example.food.presentation.foodItemScreen.FoodItemViewModel
import com.example.food.presentation.mainScreen.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var component: AppComponent

    @Inject
    protected lateinit var providerFactory: ViewModelProviderFactory

    @Inject
    lateinit var factory: FoodItemViewModel.Factory

    private val foodViewModel: FoodViewModel by viewModels {
        providerFactory
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component = DaggerAppComponent.factory().create(applicationContext)
        component.inject(this)

        setContent {
            FoodApp()
        }
    }

    @Composable
    fun FoodScreen(
        navController: NavController
    ) {
        val categoriesState = foodViewModel.categoryState.observeAsState().value
        val foodState = foodViewModel.foodState.observeAsState().value
        val selectedCategory = foodViewModel.selectedCategory.observeAsState().value
        val selectedCity = foodViewModel.selectedCity.observeAsState().value

        when (categoriesState) {
            CategoryState.InitialLoading -> LoadingScreen()
            is CategoryState.CategoryData -> Food(
                navController = navController,
                categories = categoriesState.categories,
                selectedCategory = selectedCategory,
                foodState = foodState,
                onCategorySelected = { foodViewModel.changeCategory(it) },
                onReloadFood = { foodViewModel.reloadFoods() },
                selectedCity = selectedCity!!
            )

            CategoryState.InitialLoadingError -> ErrorScreen(
                onButtonClicked = { foodViewModel.initialLoading() }
            )

            else -> {}
        }
    }

    @Composable
    private fun FoodNavHost(
        navController: NavHostController,
        modifier: Modifier = Modifier
    ) {
        NavHost(
            navController = navController,
            startDestination = FoodBottomMenuItem.Menu.route,
            modifier = modifier
        ) {
            composable(FoodBottomMenuItem.Menu.route) {
                FoodScreen(navController)
            }
            composable(FoodBottomMenuItem.Profile.route) {
                //TODO: to add new screen
            }
            composable(FoodBottomMenuItem.ShoppingCart.route) {
                val checkoutViewModel: CheckoutViewModel = viewModel(factory = providerFactory)

                CheckoutScreen(
                    onBackButtonClick = {navController.popBackStack()},
                    checkoutViewModel = checkoutViewModel
                )
            }
            composable(FoodBottomMenuItem.Cities.route) {
                CitiesList(
                    onCitySelected = {
                        navController.popBackStack()
                        foodViewModel.changeCity(it)
                    },
                )
            }
            composable(
                FoodBottomMenuItem.Banners.route,
                arguments = listOf(
                    navArgument("steps") { type = NavType.IntType },
                    navArgument("currentStep") { type = NavType.IntType },
                )
            ) { backStackEntry ->
                val bannerViewModel: BannerViewModel = viewModel(factory = providerFactory)

                BannerScreen(
                    navController = navController,
                    steps = backStackEntry.arguments!!.getInt("steps"),
                    index = backStackEntry.arguments!!.getInt("currentStep"),
                    bannerViewModel = bannerViewModel
                )
            }
            composable(
                FoodBottomMenuItem.FoodItem.route,
                arguments = listOf(
                    navArgument(FOOD_ITEM_ID) { type = NavType.LongType }
                )
            ) {
                val viewModel: FoodItemViewModel by it.assistedViewModel { saveStateHandle ->
                    factory.create(
                        saveStateHandle
                    )
                }
                FoodItemScreen(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }


    @Composable
    private fun FoodApp() {
        val navController = rememberNavController()

        Scaffold(
            modifier = Modifier,
            bottomBar = {
                FoodBottomNavigationMenu(
                    navController = navController
                )
            }
        ) { innerPadding ->
            FoodNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
