package com.std.repository

import com.std.dto.ExpenseResponse
import com.std.model.Expense
import com.std.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository

interface ExpenseRepository: JpaRepository<Expense, Long> {
    fun findAllByUser(user: User): List<ExpenseResponse>
    fun findAllByUser(user: User, pageRequest: PageRequest): Page<ExpenseResponse>
    fun findByIdAndUser(id: Long, user: User): Expense?
}