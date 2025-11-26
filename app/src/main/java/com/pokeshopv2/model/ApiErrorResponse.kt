package com.pokeshopv2.model

/**
 * Representa la estructura estándar de un mensaje de error JSON
 * enviado por Spring Boot cuando ocurre una excepción no controlada.
 */
data class ApiErrorResponse(
    val message: String? // Nos interesa principalmente el campo "message"
)
