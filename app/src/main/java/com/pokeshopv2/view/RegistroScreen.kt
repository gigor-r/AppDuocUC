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
import com.pokeshopv2.viewmodel.UsuarioRegistroViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun RegistroScreen(
    navController: NavController,
    viewModel: UsuarioRegistroViewModel
) {
    val estado by viewModel.estado.collectAsState()
    val cargando by viewModel.cargando.collectAsState()
    val registroExitoso by viewModel.registroExitoso.collectAsState()
    val errorApi by viewModel.errorApi.collectAsState()

    var confirmPassword by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(registroExitoso) {
        if (registroExitoso) {
            navController.navigate("login")
            viewModel.limpiarCampos()
            confirmPassword = ""
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
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Registro",
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Color.DarkGray
                )

                Spacer(Modifier.height(16.dp))

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
                    value = estado.correo,
                    onValueChange = viewModel::onCorreoChange,
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = estado.errores.correo != null,
                    enabled = !cargando,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    supportingText = {
                        AnimatedVisibility(visible = estado.errores.correo != null) {
                            estado.errores.correo?.let {
                                Text(it, color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = estado.contrasena,
                    onValueChange = {
                        viewModel.onContrasenaChange(it)
                        confirmPasswordError = null
                    },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = estado.errores.contrasena != null,
                    enabled = !cargando,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
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
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        confirmPasswordError = null
                    },
                    label = { Text("Confirmar Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = confirmPasswordError != null,
                    enabled = !cargando,
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(imageVector = image, contentDescription = "Mostrar/Ocultar contraseña")
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    supportingText = {
                        AnimatedVisibility(visible = confirmPasswordError != null) {
                            confirmPasswordError?.let {
                                Text(it, color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                )
                Spacer(Modifier.height(16.dp))

                AnimatedVisibility(visible = errorApi != null) {
                    errorApi?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.height(8.dp))
                    }
                }

                Button(
                    onClick = {
                        if (estado.contrasena != confirmPassword) {
                            confirmPasswordError = "Las contraseñas no coinciden"
                        } else {
                            viewModel.registrarUsuario()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !cargando,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.DarkGray
                    )
                ) {
                    Text(
                        "Registrarse",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Spacer(Modifier.height(8.dp))
                TextButton(
                    onClick = { navController.navigate("login") },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        "¿Tienes una cuenta? Inicia Sesión",
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
                            add(GifDecoder.Factory()) // Corregido aquí
                        }
                    }
                    .build()

                AsyncImage(
                    model = R.drawable.pikachurunning,
                    contentDescription = "Registrando...",
                    imageLoader = imageLoader,
                    modifier = Modifier.size(200.dp)
                )
            }
        }
    }
}
