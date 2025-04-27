package com.std.exception

class ResourceNotFoundException(message: String): RuntimeException(message)
class InvalidRequestException(message: String): RuntimeException(message)

data class ErrorResponse (
    val message: String,
    val status: Int
)