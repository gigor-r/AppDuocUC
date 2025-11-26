package com.pokeshopv2.model

import com.google.gson.annotations.SerializedName


data class Producto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("precio")
    val precio: Double,

    @SerializedName("imagen")
    val imagen: String?,

    @SerializedName("descripcion")
    val descripcion: String?,

    @SerializedName("tasa_captura")
    val tasaCaptura: Double?,

    @SerializedName("curacion")
    val curacion: Int?,

    @SerializedName("potencia")
    val potencia: Int?,

    @SerializedName("tipo")
    val tipo: String?,

    @SerializedName("stock")
    val stock: Int,

    @SerializedName("categoria")
    val categoria: Categoria
)
