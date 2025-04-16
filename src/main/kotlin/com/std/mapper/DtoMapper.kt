package com.std.mapper

import com.std.dto.RegisterRequest
import com.std.model.User

fun RegisterRequest.toUser(hashedPassword: String) = User(
    name = this.name,
    email = this.email,
    password = hashedPassword
)
