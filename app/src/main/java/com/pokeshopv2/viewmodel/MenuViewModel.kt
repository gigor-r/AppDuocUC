package com.pokeshopv2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pokeshopv2.model.Producto
import com.pokeshopv2.network.ApiConfig
import com.pokeshopv2.network.ProductoApiService
import com.pokeshopv2.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

private const val TAG = "MenuViewModel"
class MenuViewModel : ViewModel() {


    private val _productos = MutableStateFlow<List<Producto>>(emptyList())

    private val _productosFiltrados = MutableStateFlow<List<Producto>>(emptyList())
    val productosFiltrados: StateFlow<List<Producto>> = _productosFiltrados.asStateFlow()

    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _errorApi = MutableStateFlow<String?>(null)
    val errorApi: StateFlow<String?> = _errorApi.asStateFlow()

    private val apiService by lazy {
        RetrofitInstance.getService(ProductoApiService::class.java, ApiConfig.BASE_URL_PRODUCTOS)
    }

    init {
        cargarProductos()
    }

    private fun cargarProductos() {
        viewModelScope.launch {
            _cargando.value = true
            _errorApi.value = null
            try {
                Log.d(TAG, "Iniciando carga de productos desde ${ApiConfig.BASE_URL_PRODUCTOS}")
                val response = apiService.obtenerTodosLosProductos()
                if (response.isSuccessful) {
                    val listaCompleta = response.body() ?: emptyList()
                    Log.d(TAG, "Productos cargados: ${listaCompleta.size}")
                    _productos.value = listaCompleta
                    _productosFiltrados.value = listaCompleta
                } else {
                    val errorMsg = "Error HTTP ${response.code()}: ${response.message()}"
                    Log.e(TAG, errorMsg)
                    _errorApi.value = errorMsg
                }
            } catch (e: Exception) {
                val errorMsg = "Excepción: ${e.message}"
                Log.e(TAG, errorMsg, e)
                _errorApi.value = errorMsg
            } finally {
                _cargando.value = false
            }
        }
    }

    fun filtrarProductos(query: String) {
        if (query.isEmpty()) {
            _productosFiltrados.value = _productos.value
        } else {
            _productosFiltrados.value = _productos.value.filter {
                it.nombre.contains(query, ignoreCase = true)
            }
        }
    }

    fun limpiarError() {
        _errorApi.value = null
    }

    fun recargarProductos() {
        cargarProductos()
    }
}
