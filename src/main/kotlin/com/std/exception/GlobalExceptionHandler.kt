package com.std.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import kotlin.math.log


// This class is a centralized exception handler

@ControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(e: ResourceNotFoundException, request: WebRequest): ResponseEntity<Any> {
        return ResponseEntity(ErrorResponse(message = e.message ?: " Resource Not found", HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(InvalidRequestException::class)
    fun handleInvalidRequestException(e: InvalidRequestException, request: WebRequest): ResponseEntity<Any> {
        return ResponseEntity(ErrorResponse(message = e.message ?: "Invalid Request", status = HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(e: Exception, request: WebRequest): ResponseEntity<Any> {
        logger.error("Generic exception message: ${e.message}")
        return ResponseEntity(e.message ?: "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR)
    }
}