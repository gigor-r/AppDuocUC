package com.pokeshopv2.validation

import android.util.Patterns
import com.pokeshopv2.model.Usuario
import com.pokeshopv2.model.UsuarioErrores

fun validarRegistro(usuario: Usuario): UsuarioErrores {
    val errores = UsuarioErrores(
        nombre = if (usuario.nombre.isBlank()) "Nombre no puede estar vacío" else null,
        correo = if (usuario.correo.isBlank()) {
            "Correo no puede estar vacío"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(usuario.correo).matches()) {
            "Formato de correo no es válido"
        } else null,
        contrasena = if (usuario.contrasena.length < 6) "Contraseña debe tener al menos 6 caracteres" else null
    )
    return errores
}