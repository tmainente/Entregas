package com.example.entregas.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.entregas.presentation.screen.DeliveryFormScreen
import com.example.entregas.presentation.screen.DeliveryScreen
import kotlinx.serialization.json.Json
import java.net.URLEncoder

sealed class Screen(val route: String) {
    data object List : Screen("list")
    data object Form : Screen("form/{deliveryJson}") {
        fun createRoute(deliveryJson: String) = "form/$deliveryJson"
    }
}


@Composable
fun DeliveryNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.List.route) {
        composable(route = Screen.List.route) {
            DeliveryScreen(
                onNavigateToForm = { delivery ->
                    val deliveryJson = delivery?.let {
                        URLEncoder.encode(Json.encodeToString(it), "UTF-8")
                    } ?: ""
                    navController.navigate(Screen.Form.createRoute(deliveryJson))
                }
            )
        }
        composable(
            route = Screen.Form.route,
            arguments = listOf(
                navArgument("deliveryJson") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val deliveryJson = backStackEntry.arguments?.getString("deliveryJson")
            DeliveryFormScreen(
                deliveryJson = deliveryJson,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
