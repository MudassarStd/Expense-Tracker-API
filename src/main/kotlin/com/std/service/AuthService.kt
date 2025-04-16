package com.std.service

import com.std.dto.AuthResponse
import com.std.dto.LoginRequest
import com.std.dto.RegisterRequest
import com.std.mapper.toUser
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import kotlin.math.log

@Service
class AuthService(
    private val userService: UserService,
    private val jwtService: JwtService,
    private val passwordEncoder: BCryptPasswordEncoder
) {

    fun register(registerRequest: RegisterRequest): AuthResponse {
        // if email is already registered
        if (userService.existsByEmail(registerRequest.email)) {
            throw IllegalArgumentException("Email is already in use")
        }
        val hashedPassword = passwordEncoder.encode(registerRequest.password)

        // create a user from register request
        val newUser = registerRequest.toUser(hashedPassword)

        // save user
        userService.save(newUser)

        // generate token
        val token = jwtService.generateToken(newUser.email)

        // response with token
        return AuthResponse(token)
    }
    fun login(loginRequest: LoginRequest): AuthResponse {

        // find user
        val user = userService.findByEmail(loginRequest.email)

        // validate password
        if (!passwordEncoder.matches(loginRequest.password, user.password)) {
            throw BadCredentialsException("Invalid password")
        }


        val token = jwtService.generateToken(user.email)

        return AuthResponse(token)
    }
}