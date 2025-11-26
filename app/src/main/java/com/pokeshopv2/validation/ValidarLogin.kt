package com.pokeshopv2.validation

import com.pokeshopv2.model.Usuario
import com.pokeshopv2.model.UsuarioErrores

fun validarLogin(usuario: Usuario): UsuarioErrores {
  val errores = UsuarioErrores(
    nombre = if (usuario.nombre.isBlank()) "Nombre no puede estar vacío" else null,
    contrasena = if (usuario.contrasena.length < 6) "Contraseña debe tener al menos 6 caracteres" else null
  )
  return errores
}