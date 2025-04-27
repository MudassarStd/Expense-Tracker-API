package com.std.mapper

import com.std.dto.ExpenseRequest
import com.std.dto.ExpenseResponse
import com.std.dto.RegisterRequest
import com.std.model.Expense
import com.std.model.User

fun RegisterRequest.toUser(hashedPassword: String) = User(
    name = this.name,
    email = this.email,
    password = hashedPassword
)

fun ExpenseRequest.toExpense(user: User) = Expense (
    amount = this.amount,
    category = this.category,
    type = this.type,
    description = this.description,
    date = this.date,
    user = user
)

fun Expense.toExpenseResponse() = ExpenseResponse(
    id = this.id,
    amount = this.amount,
    category = this.category,
    type = this.type,
    description = this.description,
    date = this.date,
)