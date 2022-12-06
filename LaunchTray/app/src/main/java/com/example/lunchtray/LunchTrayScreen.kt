/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lunchtray

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lunchtray.datasource.DataSource
import com.example.lunchtray.ui.*

enum class LaunchScreen(@StringRes val title:Int) {
    Start(title = R.string.start_order),
    MainMenu(title = R.string.choose_entree),
    SideMenu(title = R.string.choose_side_dish),
    DessertMenu(title = R.string.choose_accompaniment),
    Calculation(title = R.string.order_checkout)
}

@Composable
fun LaunchTrayAppBar(
    currentScreen: LaunchScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar (
        title = { Text(stringResource(id = currentScreen.title)) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )

}

@Composable
fun LunchTrayApp(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier) {

    val viewModel: OrderViewModel = viewModel()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = LaunchScreen.valueOf(
        backStackEntry?.destination?.route ?: LaunchScreen.Start.name
    )

    Scaffold(
        topBar = {
            LaunchTrayAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = LaunchScreen.Start.name,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(route = LaunchScreen.Start.name) {
                StartOrderScreen(onStartOrderButtonClicked = {
                    navController.navigate(LaunchScreen.MainMenu.name)
                })
            }
            composable(route = LaunchScreen.MainMenu.name) {
                EntreeMenuScreen(
                    options = DataSource.entreeMenuItems,
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    },
                    onNextButtonClicked = {
                        navController.navigate(LaunchScreen.SideMenu.name)
                    },
                    onSelectionChanged = {
                        viewModel.updateEntree(it)
                    }
                )
            }
            composable(route = LaunchScreen.SideMenu.name) {
                SideDishMenuScreen(
                    options = DataSource.sideDishMenuItems,
                    onNextButtonClicked = {
                        navController.navigate(LaunchScreen.DessertMenu.name)
                    },
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    },
                    onSelectionChanged = {
                        viewModel.updateSideDish(it)
                    },
                )
            }
            composable(route = LaunchScreen.DessertMenu.name) {
                AccompanimentMenuScreen(
                    options = DataSource.accompanimentMenuItems,
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    },
                    onNextButtonClicked = {
                        navController.navigate(LaunchScreen.Calculation.name)
                    },
                    onSelectionChanged = {
                        viewModel.updateAccompaniment(it)
                    }
                )
            }
            composable(route = LaunchScreen.Calculation.name) {
                CheckoutScreen(
                    orderUiState = uiState,
                    onNextButtonClicked = { /*TODO*/ },
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    }
                )
            }
        }
    }
}

private fun cancelOrderAndNavigateToStart (
    viewModel: OrderViewModel,
    navController: NavHostController
) {
    viewModel.resetOrder()
    navController.popBackStack(LaunchScreen.Start.name, inclusive = false)
}
