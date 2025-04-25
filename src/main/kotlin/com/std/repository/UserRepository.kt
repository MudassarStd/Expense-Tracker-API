package com.std.repository

import com.std.model.User
import jakarta.validation.constraints.Email
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): User?

    fun findByName(name: String): User?
}