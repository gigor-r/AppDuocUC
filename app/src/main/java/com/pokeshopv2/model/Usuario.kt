package com.pokeshopv2.model

data class Usuario (
  val id : Int? = null,
  val nombre : String ="",
  val correo : String ="",
  val contrasena : String ="",
  val rol: Rol,
  val errores : UsuarioErrores = UsuarioErrores()
) {
    companion object {
        fun default(): Usuario {
            return Usuario(
                id = null,
                nombre = "",
                correo = "",
                contrasena = "",
                rol = Rol.default(),
                errores = UsuarioErrores()
            )
        }
    }
}