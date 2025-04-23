package com.std.dto

import com.std.model.Category
import com.std.model.Expense
import com.std.model.Type
import java.time.LocalDate

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val token: String
)

data class ExpenseRequest(
    val amount: Double,
    val description: String,
    val category: Category,
    val type: Type,
    val date: LocalDate
)

data class PaginatedResponse(
    val expenses: List<Expense>,
    val hasNextPage: Boolean = true
)