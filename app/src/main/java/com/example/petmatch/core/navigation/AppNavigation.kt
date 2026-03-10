package com.example.petmatch.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.petmatch.features.auth.presentation.screens.*
import com.example.petmatch.features.petmatch.presentation.screens.*
import com.example.petmatch.features.health.presentation.screens.PetHealthScreen
import com.example.petmatch.features.interest.presentation.screens.PetInterestsScreen
import com.example.petmatch.features.petmatch.presentation.viewmodels.DashboardViewModel
import com.example.petmatch.features.interest.presentation.viewmodels.InterestViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = PetMatchScreens.Login
    ) {

        composable<PetMatchScreens.Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(PetMatchScreens.Dashboard) {
                        popUpTo(PetMatchScreens.Login) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(PetMatchScreens.Register) }
            )
        }

        composable<PetMatchScreens.Register> {
            RegisterScreen(
                onRegisterSuccess = { navController.popBackStack() },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable<PetMatchScreens.Dashboard> { backStackEntry ->
            val interestViewModel: InterestViewModel = hiltViewModel()

            DashboardScreen(
                onNavigateToAddPet = { navController.navigate(PetMatchScreens.AddPet) },
                onNavigateToAddHome = { navController.navigate(PetMatchScreens.AddHome) },
                // ACTUALIZADO: Ahora recibe 5 parámetros, incluyendo la URL de la foto (fUrl)
                onNavigateToEditPet = { id, n, s, a, fUrl -> navController.navigate(PetMatchScreens.EditPet(id, n, s, a, fUrl)) },
                onNavigateToEditHome = { id, n, d, c, t -> navController.navigate(PetMatchScreens.EditHome(id, n, d, c, t)) },
                onNavigateToAssign = { id, name -> navController.navigate(PetMatchScreens.AssignPet(id, name)) },
                onNavigateToHealth = { id, name -> navController.navigate(PetMatchScreens.HealthHistory(id, name)) },
                onToggleInterest = { petId, status -> interestViewModel.toggleInterest(petId, status) },
                onNavigateToInterests = { id, name -> navController.navigate(PetMatchScreens.PetInterests(id, name)) }
            )
        }

        composable<PetMatchScreens.AddPet> { PetFormScreen(onBack = { navController.popBackStack() }) }

        composable<PetMatchScreens.EditPet> { backStackEntry ->
            val args = backStackEntry.toRoute<PetMatchScreens.EditPet>()
            PetFormScreen(
                petId = args.id,
                initialName = args.name,
                initialSpecie = args.specie,
                initialAge = args.age.toString(),
                initialFotoUrl = args.fotoUrl, // ACTUALIZADO: Le pasamos la foto al formulario
                onBack = { navController.popBackStack() }
            )
        }

        composable<PetMatchScreens.AddHome> { HomeFormScreen(onBack = { navController.popBackStack() }) }

        composable<PetMatchScreens.EditHome> { backStackEntry ->
            val args = backStackEntry.toRoute<PetMatchScreens.EditHome>()
            HomeFormScreen(homeId = args.id, initialName = args.name, initialDir = args.dir, initialCap = args.cap.toString(), initialType = args.type, onBack = { navController.popBackStack() })
        }

        composable<PetMatchScreens.AssignPet> { backStackEntry ->
            val args = backStackEntry.toRoute<PetMatchScreens.AssignPet>()
            val dashboardEntry = remember(backStackEntry) { navController.getBackStackEntry(PetMatchScreens.Dashboard) }
            val sharedDashboardViewModel: DashboardViewModel = hiltViewModel(dashboardEntry)
            AssignPetScreen(petId = args.petId, petName = args.petName, dashboardViewModel = sharedDashboardViewModel, onBack = { navController.popBackStack() })
        }

        composable<PetMatchScreens.HealthHistory> { backStackEntry ->
            val args = backStackEntry.toRoute<PetMatchScreens.HealthHistory>()
            PetHealthScreen(petId = args.petId, petName = args.petName, onBack = { navController.popBackStack() })
        }

        composable<PetMatchScreens.PetInterests> { backStackEntry ->
            val args = backStackEntry.toRoute<PetMatchScreens.PetInterests>()
            PetInterestsScreen(petId = args.petId, petName = args.petName, onBack = { navController.popBackStack() })
        }
    }
}