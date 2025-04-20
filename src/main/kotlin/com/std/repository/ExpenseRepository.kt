package com.std.repository

import com.std.model.Expense
import org.springframework.data.jpa.repository.JpaRepository

interface ExpenseRepository: JpaRepository<Expense, Long>