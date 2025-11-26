package com.pokeshopv2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pokeshopv2.model.Usuario
import com.pokeshopv2.network.ApiConfig
import com.pokeshopv2.network.UsuarioApiService
import com.pokeshopv2.network.RetrofitInstance
import com.pokeshopv2.validation.validarLogin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsuarioLoginViewModel : ViewModel() {
    private val _estado = MutableStateFlow(Usuario.default())
    val estado: StateFlow<Usuario> = _estado

    private val _cargando = MutableStateFlow(false)
    val cargando = _cargando.asStateFlow()

    private val _loginExitoso = MutableStateFlow(false)
    val loginExitoso = _loginExitoso.asStateFlow()

    private val _errorApi = MutableStateFlow<String?>(null)
    val errorApi = _errorApi.asStateFlow()

    private val apiService = RetrofitInstance.getService(UsuarioApiService::class.java, ApiConfig.BASE_URL_USUARIOS)

    fun onNombreChange(value: String) {
        _estado.update { it.copy(nombre = value, errores = it.errores.copy(nombre = null)) }
        _errorApi.value = null
    }

    fun onContrasenaChange(value: String) {
        _estado.update { it.copy(contrasena = value, errores = it.errores.copy(contrasena = null)) }
        _errorApi.value = null
    }

    private fun validarFormularioLocal(): Boolean {
        val estadoActual = _estado.value
        val errores = validarLogin(estadoActual)
        val hayErrores = listOfNotNull(
            errores.nombre,
            errores.contrasena
        ).isNotEmpty()
        _estado.update { it.copy(errores = errores) }
        return !hayErrores
    }

    fun iniciarLogin() {
        if (!validarFormularioLocal()) {
            return
        }

        viewModelScope.launch {
            _cargando.value = true
            _errorApi.value = null
            try {
                val response = apiService.login(_estado.value)
                if (response.isSuccessful && response.body() != null) {
                    _loginExitoso.value = true
                } else {
                    _errorApi.value = "Nombre de usuario o contraseña incorrectos."
                    _cargando.value = false
                }
            } catch (e: Exception) {
                _errorApi.value = "No se pudo conectar al servidor. Inténtalo más tarde."
                _cargando.value = false
            }
        }
    }

    fun limpiarCampos() {
        _estado.value = Usuario.default()
    }

    fun onNavegacionCompleta() {
        _loginExitoso.value = false
    }

    fun reiniciarEstadoCarga() {
        _cargando.value = false
    }
}
