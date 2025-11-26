package com.pokeshopv2.view

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.pokeshopv2.R
import com.pokeshopv2.viewmodel.UsuarioLoginViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: UsuarioLoginViewModel
) {
    val estado by viewModel.estado.collectAsState()
    val cargando by viewModel.cargando.collectAsState()
    val loginExitoso by viewModel.loginExitoso.collectAsState()
    val errorApi by viewModel.errorApi.collectAsState()
    val focusManager = LocalFocusManager.current
    var passwordVisible by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.reiniciarEstadoCarga()
        }
    }

    LaunchedEffect(loginExitoso) {
        if (loginExitoso) {
            navController.navigate("menu") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
            viewModel.limpiarCampos()
            viewModel.onNavegacionCompleta()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.DarkGray,
                    ),
                    title = {
                        Text(
                            text = "PokéShop",
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp
                        )
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { focusManager.clearFocus() }
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Login",
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = estado.nombre,
                    onValueChange = viewModel::onNombreChange,
                    label = { Text("Nombre de usuario") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = estado.errores.nombre != null,
                    enabled = !cargando,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    supportingText = {
                        AnimatedVisibility(visible = estado.errores.nombre != null) {
                            estado.errores.nombre?.let {
                                Text(it, color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = estado.contrasena,
                    onValueChange = viewModel::onContrasenaChange,
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = estado.errores.contrasena != null,
                    enabled = !cargando,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if (passwordVisible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = "Mostrar/Ocultar contraseña")
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    supportingText = {
                        AnimatedVisibility(visible = estado.errores.contrasena != null) {
                            estado.errores.contrasena?.let {
                                Text(it, color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                )
                Spacer(Modifier.height(15.dp))

                AnimatedVisibility(visible = errorApi != null) {
                    errorApi?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.height(8.dp))
                    }
                }

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.iniciarLogin()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !cargando,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.DarkGray
                    )
                ) {
                    Text(
                        "Iniciar Sesión",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Spacer(Modifier.height(10.dp))

                TextButton(
                    onClick = { navController.navigate("registro") },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    enabled = !cargando
                ) {
                    Text(
                        "¿No tienes cuenta? Regístrate aquí",
                        color = Color.Blue
                    )
                }
            }
        }

        if (cargando) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {},
                contentAlignment = Alignment.Center
            ) {
                val context = LocalContext.current
                val imageLoader = ImageLoader.Builder(context)
                    .components {
                        if (Build.VERSION.SDK_INT >= 28) {
                            add(ImageDecoderDecoder.Factory())
                        } else {
                            add(GifDecoder.Factory())
                        }
                    }
                    .build()

                AsyncImage(
                    model = R.drawable.pikachurunning,
                    contentDescription = "Iniciando sesión...",
                    imageLoader = imageLoader,
                    modifier = Modifier.size(200.dp)
                )
            }
        }
    }
}
