package com.pokeshopv2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.pokeshopv2.model.ApiErrorResponse
import com.pokeshopv2.model.Rol
import com.pokeshopv2.model.Usuario
import com.pokeshopv2.network.ApiConfig
import com.pokeshopv2.network.UsuarioApiService
import com.pokeshopv2.network.RetrofitInstance
import com.pokeshopv2.validation.validarRegistro
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsuarioRegistroViewModel : ViewModel() {
    private val _estado = MutableStateFlow(Usuario.default())
    val estado: StateFlow<Usuario> = _estado

    private val _cargando = MutableStateFlow(false)
    val cargando = _cargando.asStateFlow()

    private val _registroExitoso = MutableStateFlow(false)
    val registroExitoso = _registroExitoso.asStateFlow()

    private val _errorApi = MutableStateFlow<String?>(null)
    val errorApi = _errorApi.asStateFlow()

    private val apiService = RetrofitInstance.getService(UsuarioApiService::class.java, ApiConfig.BASE_URL_USUARIOS)

    fun onNombreChange(value: String) {
        _estado.update { it.copy(nombre = value, errores = it.errores.copy(nombre = null)) }
        _errorApi.value = null
    }

    fun onCorreoChange(value: String) {
        _estado.update { it.copy(correo = value, errores = it.errores.copy(correo = null)) }
        _errorApi.value = null
    }

    fun onContrasenaChange(value: String) {
        _estado.update { it.copy(contrasena = value, errores = it.errores.copy(contrasena = null)) }
        _errorApi.value = null
    }

    private fun validarFormularioLocal(): Boolean {
        val estadoActual = _estado.value
        val errores = validarRegistro(estadoActual)
        val hayErrores = listOfNotNull(
            errores.nombre,
            errores.correo,
            errores.contrasena
        ).isNotEmpty()
        _estado.update { it.copy(errores = errores) }
        return !hayErrores
    }

    fun registrarUsuario() {
        if (!validarFormularioLocal()) {
            return
        }

        viewModelScope.launch {
            _errorApi.value = null
            try {
                val rolDeUsuario = Rol(id = 1, nombre = "Usuario")
                val usuarioParaEnviar = _estado.value.copy(rol = rolDeUsuario)

                val response = apiService.registrarUsuario(usuarioParaEnviar)
                if (response.isSuccessful) {
                    _registroExitoso.value = true
                } else {
                    val errorBody = response.errorBody()?.string()
                    if (errorBody != null) {
                        try {
                            val gson = Gson()
                            val errorResponse = gson.fromJson(errorBody, ApiErrorResponse::class.java)
                            if (errorResponse.message?.contains("correo", ignoreCase = true) == true) {
                                _estado.update { it.copy(errores = it.errores.copy(correo = "El correo ya se encuentra registrado")) }
                            } else {
                                _errorApi.value = errorResponse.message ?: "Error desconocido"
                            }
                        } catch (e: Exception) {
                            _errorApi.value = "Error al interpretar la respuesta del servidor."
                        }
                    } else {
                        _errorApi.value = "Error en el registro"
                    }
                }
            } catch (e: Exception) {
                _errorApi.value = "Error al registrar. El correo podría ya estar en uso o hay un problema de conexión."
            }
        }
    }

    fun limpiarCampos() {
        _estado.value = Usuario.default()
    }

    fun onNavegacionCompleta() {
        _registroExitoso.value = false
    }
}
