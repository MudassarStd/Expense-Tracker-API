package com.std.controller

import com.std.model.Category
import com.std.model.Expense
import com.std.model.User
import com.std.service.ExpenseService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/expense")
class ExpenseController(private val expenseService: ExpenseService) {

    @GetMapping
    fun getAll() = expenseService.findAll()

    @PostMapping
    fun add() = expenseService.add(Expense(
        amount = 12.0,
        description = "this",
        category = Category.HEALTH,
    ))
}