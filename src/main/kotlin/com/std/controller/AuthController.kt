package com.std.controller

import com.std.dto.AuthResponse
import com.std.dto.LoginRequest
import com.std.dto.RegisterRequest
import com.std.service.AuthService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {
    private val logger = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("/register")
    fun register(@RequestBody @Valid registerRequest: RegisterRequest): ResponseEntity<AuthResponse> {
        logger.info("Registration request: $registerRequest")
        return ResponseEntity.ok(authService.register(registerRequest))
    }

    @PostMapping("login")
    fun login(@RequestBody @Valid loginRequest: LoginRequest): ResponseEntity<AuthResponse> {
        logger.info("Login request: $loginRequest")
        return ResponseEntity.ok(authService.authenticate(loginRequest))
    }
}