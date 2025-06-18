package com.example.entregas.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.entregas.presentation.screen.DeliveryFormScreen
import com.example.entregas.presentation.screen.DeliveryScreen

sealed class Screen(val route: String) {
    data object DeliveryList : Screen("delivery_list")
    data object DeliveryForm : Screen("delivery_form?deliveryId={deliveryId}") {
        fun withId(id: Long?) = "delivery_form?deliveryId=${id ?: -1}"
    }
}

@Composable
fun DeliveryNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.DeliveryList.route) {
        composable(Screen.DeliveryList.route) {
            DeliveryScreen(onNavigateToForm = { deliveryId ->
                navController.navigate(Screen.DeliveryForm.withId(deliveryId))
            })
        }
        composable(
            route = Screen.DeliveryForm.route,
            arguments = listOf(navArgument("deliveryId") {
                type = NavType.LongType
                defaultValue = -1L
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("deliveryId") ?: -1L
            DeliveryFormScreen(
                deliveryId = if (id != -1L) id else null,
                onBack = { navController.popBackStack() }
            )
        }
    }
}