package com.std.exception

import org.aspectj.bridge.Message

class ResourceNotFoundException(message: String): RuntimeException(message)
class InvalidRequestException(message: String): RuntimeException(message)

data class ErrorResponse (
    val message: String,
    val status: Int
)