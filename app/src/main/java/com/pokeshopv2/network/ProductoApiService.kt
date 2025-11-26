package com.pokeshopv2.network

import com.pokeshopv2.model.Producto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductoApiService {

  @GET("producto")
  suspend fun obtenerTodosLosProductos(): Response<List<Producto>>

  @GET("producto/{id}")
  suspend fun obtenerProductoPorId(@Path("id") id: Int): Response<Producto>

  @GET("producto/buscar")
  suspend fun buscarProductos(@Query("q") query: String): Response<List<Producto>>

  @GET("producto/categoria/{categoriaId}")
  suspend fun obtenerProductosPorCategoria(@Path("categoriaId") categoriaId: Int): Response<List<Producto>>
}