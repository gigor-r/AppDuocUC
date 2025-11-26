package com.pokeshopv2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pokeshopv2.ui.theme.PokeShopv2Theme
import com.pokeshopv2.view.CarroScreen
import com.pokeshopv2.view.LoginScreen
import com.pokeshopv2.view.MenuScreen
import com.pokeshopv2.view.QrScreen
import com.pokeshopv2.view.PagoScreen
import com.pokeshopv2.view.RegistroScreen
import com.pokeshopv2.view.confirmarPago
import com.pokeshopv2.viewmodel.CarroViewModel
import com.pokeshopv2.viewmodel.MenuViewModel
import com.pokeshopv2.viewmodel.UsuarioLoginViewModel
import com.pokeshopv2.viewmodel.UsuarioRegistroViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val usuarioRegistroViewModel: UsuarioRegistroViewModel by viewModels()
        val usuarioLoginViewModel: UsuarioLoginViewModel by viewModels()
        val menuViewModel: MenuViewModel by viewModels()
        val carroViewModel: CarroViewModel by viewModels()

        setContent {
            PokeShopv2Theme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable("registro") {
                        RegistroScreen(navController, usuarioRegistroViewModel)
                    }
                    composable("login") {
                        LoginScreen(navController, usuarioLoginViewModel)
                    }
                    composable("menu") {
                        MenuScreen(navController, menuViewModel, carroViewModel)
                    }
                    composable("qr") {
                        QrScreen(navController)
                    }
                    composable("Carrito") {
                        CarroScreen(navController, carroViewModel) { navController.popBackStack() }
                    }
                    composable("pago"){
                        PagoScreen(carroViewModel,onBack = {navController.popBackStack()},
                        navController = navController, onConfirm = {navController.popBackStack()})
                    }
                    composable("confirmarPago") {
                        confirmarPago(navController)
                    }
                }
            }
        }
    }
}
