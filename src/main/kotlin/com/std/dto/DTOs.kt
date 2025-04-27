package com.std.dto

import com.std.model.Category
import com.std.model.Expense
import com.std.model.Type
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

data class RegisterRequest(
    val name: String,
    @field:Email
    val email: String,
    @field:NotBlank
    val password: String
)

data class LoginRequest(
    @field:Email
    val email: String,
    @field:NotBlank
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

data class ExpenseResponse(
    val id: Long,
    val amount: Double,
    val description: String,
    val category: Category,
    val type: Type,
    val date: LocalDate,
)

data class PaginatedResponse(
    val expenses: List<ExpenseResponse>,
    val hasNextPage: Boolean = true
)