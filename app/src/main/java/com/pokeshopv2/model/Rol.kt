package com.pokeshopv2.model

data class Rol (
    val id : Int? = null,
    val nombre: String,
){
    companion object{
        fun default() : Rol{
            return Rol(
                null,
                ""
            )
        }
    }
}
