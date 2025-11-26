package com.pokeshopv2.network

import com.pokeshopv2.model.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET

interface UsuarioApiService {

    @POST("usuario")
    suspend fun registrarUsuario(@Body usuario: Usuario): Response<Usuario>

    @POST("usuario/login")
    suspend fun login(@Body credenciales: Usuario): Response<Usuario>

    @GET("usuario")
    suspend fun obtenerUsuarios(): Response<List<Usuario>>
}
