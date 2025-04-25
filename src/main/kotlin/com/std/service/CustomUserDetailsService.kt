package com.std.service

import com.std.exception.ResourceNotFoundException
import com.std.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
): UserDetailsService {
    override fun loadUserByUsername(email: String?): UserDetails {
        return email?.let {
            userRepository.findByEmail(it) ?: throw ResourceNotFoundException("User not found: CustomUserDetailsService")
        } ?: throw IllegalArgumentException("username not passed to UserDetailsService")
    }
}