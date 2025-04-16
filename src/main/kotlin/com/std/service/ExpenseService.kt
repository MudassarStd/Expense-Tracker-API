package com.std.service

import com.std.model.Expense
import com.std.repository.ExpenseRepository
import org.springframework.stereotype.Service
import kotlin.math.exp

@Service
class ExpenseService(private val expenseRepository: ExpenseRepository) {
    fun findAll() = expenseRepository.findAll()
    fun add(expense: Expense) = expenseRepository.save(expense)
}