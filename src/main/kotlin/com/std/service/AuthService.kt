package com.std.service

import com.std.dto.AuthResponse
import com.std.dto.LoginRequest
import com.std.dto.RegisterRequest
import com.std.mapper.toUser
import com.std.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service


@Service
class AuthService(
    private val userRepository: UserRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager
) {

    val logger = LoggerFactory.getLogger(AuthService::class.java)

    fun register(request: RegisterRequest): AuthResponse {
        logger.info("In register")
        userRepository.save(request.toUser(bCryptPasswordEncoder.encode(request.password)))
        logger.info("after saving user")
        return AuthResponse(token = jwtService.generateToken(request.email))
    }

    fun authenticate(request: LoginRequest): AuthResponse {

        val authToken = UsernamePasswordAuthenticationToken(request.email, request.password)

        authenticationManager.authenticate(authToken)

        return AuthResponse(token = jwtService.generateToken(request.email))
    }

}