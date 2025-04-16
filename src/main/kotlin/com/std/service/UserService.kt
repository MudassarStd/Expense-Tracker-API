package com.std.service

import com.std.model.User
import com.std.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun existsByEmail(email: String): Boolean = userRepository.existsByEmail(email)
    fun save(user: User) = userRepository.save(user)

    fun findByEmail(email: String) = userRepository.findByEmail(email)
}