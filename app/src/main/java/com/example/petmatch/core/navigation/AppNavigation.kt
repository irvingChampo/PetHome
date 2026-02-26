package com.example.petmatch.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.petmatch.features.auth.presentation.screens.*
import com.example.petmatch.features.petmatch.presentation.screens.*
import com.example.petmatch.features.petmatch.presentation.viewmodels.DashboardViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = PetMatchScreens.Login.route) {

        composable(PetMatchScreens.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(PetMatchScreens.Dashboard.route) {
                        popUpTo(PetMatchScreens.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(PetMatchScreens.Register.route) }
            )
        }

        composable(PetMatchScreens.Register.route) {
            RegisterScreen(
                onRegisterSuccess = { navController.popBackStack() },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(PetMatchScreens.Dashboard.route) {
            DashboardScreen(
                onNavigateToAddPet = { navController.navigate(PetMatchScreens.AddPet.route) },
                onNavigateToAddHome = { navController.navigate(PetMatchScreens.AddHome.route) },
                onNavigateToEditPet = { id, n, s, a ->
                    navController.navigate(PetMatchScreens.EditPet.createRoute(id, n, s, a))
                },
                onNavigateToEditHome = { id, n, d, c, t ->
                    navController.navigate(PetMatchScreens.EditHome.createRoute(id, n, d, c, t))
                },
                onNavigateToAssign = { id, name ->
                    navController.navigate(PetMatchScreens.AssignPet.createRoute(id, name))
                }
            )
        }

        composable(PetMatchScreens.AddPet.route) {
            PetFormScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = PetMatchScreens.EditPet.route,
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("name") { type = NavType.StringType },
                navArgument("specie") { type = NavType.StringType },
                navArgument("age") { type = NavType.IntType }
            )
        ) { backStack ->
            PetFormScreen(
                petId = backStack.arguments?.getInt("id") ?: 0,
                initialName = backStack.arguments?.getString("name") ?: "",
                initialSpecie = backStack.arguments?.getString("specie") ?: "",
                initialAge = backStack.arguments?.getInt("age")?.toString() ?: "",
                onBack = { navController.popBackStack() }
            )
        }

        composable(PetMatchScreens.AddHome.route) {
            HomeFormScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = PetMatchScreens.EditHome.route,
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("name") { type = NavType.StringType },
                navArgument("dir") { type = NavType.StringType },
                navArgument("cap") { type = NavType.IntType },
                navArgument("type") { type = NavType.StringType }
            )
        ) { backStack ->
            HomeFormScreen(
                homeId = backStack.arguments?.getInt("id") ?: 0,
                initialName = backStack.arguments?.getString("name") ?: "",
                initialDir = backStack.arguments?.getString("dir") ?: "",
                initialCap = backStack.arguments?.getInt("cap")?.toString() ?: "",
                initialType = backStack.arguments?.getString("type") ?: "",
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = PetMatchScreens.AssignPet.route,
            arguments = listOf(
                navArgument("petId") { type = NavType.IntType },
                navArgument("petName") { type = NavType.StringType }
            )
        ) { backStack ->

            val dashboardEntry = remember(backStack) {
                navController.getBackStackEntry(PetMatchScreens.Dashboard.route)
            }
            val sharedDashboardViewModel: DashboardViewModel = hiltViewModel(dashboardEntry)

            AssignPetScreen(
                petId = backStack.arguments?.getInt("petId") ?: 0,
                petName = backStack.arguments?.getString("petName") ?: "",
                dashboardViewModel = sharedDashboardViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}