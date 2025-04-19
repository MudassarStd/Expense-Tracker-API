package com.std.service

import com.std.controller.Sort
import com.std.dto.ExpenseRequest
import com.std.mapper.toExpense
import com.std.model.Category
import com.std.model.Expense
import com.std.model.Type
import com.std.repository.ExpenseRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDate
import javax.swing.SortOrder
import kotlin.math.exp
import kotlin.math.min

@Service
class ExpenseService(private val expenseRepository: ExpenseRepository) {

    fun findAll() = expenseRepository.findAll()

    fun findById(id: Long) = expenseRepository.findById(id)

    fun add(expenseRequest: ExpenseRequest) = expenseRepository.save(expenseRequest.toExpense())

    fun addList(list: List<ExpenseRequest>) {
        val expenses = list.map { it.toExpense() }
        expenseRepository.saveAll(expenses)
    }

    fun findAllFiltered(
        minAmount: Double?,
        maxAmount: Double?,
        category: Category?,
        type: Type?,
        startDate: LocalDate?,
        endDate: LocalDate?
    ): List<Expense> {
        return expenseRepository.findAll().filter { expense ->
            (minAmount == null || expense.amount >= minAmount) &&
                    (maxAmount == null || expense.amount <= maxAmount) &&
                    (category == null || expense.category == category) &&
                    (type == null || expense.type == type) && (startDate == null || expense.date >= startDate) && (endDate == null || expense.date <= endDate)
        }
    }

    fun update(id: Long, expense: Expense): Expense? {
        return if (expenseRepository.existsById(id)) {
            expenseRepository.save(expense.copy(id = id))
        } else {
            null
        }
    }

    fun deleteById(id: Long): String {
        return if (expenseRepository.existsById(id)) {
            expenseRepository.deleteById(id)
            "Deleted successfully"
        } else {
            "Expense does not exist"
        }
    }

    fun getTotalAmount(
        startDate: LocalDate?,
        endDate: LocalDate?
    ) = expenseRepository.findAll()
        .filter { (startDate == null || it.date >= startDate) && (endDate == null || it.date <= endDate) }.sumOf { it.amount }

    fun getTotalAmountByType(type: Type) = expenseRepository.findAll().filter { it.type == type }.sumOf { it.amount }

    fun findSortedByOrder(order: Sort?): List<Expense> {
        return if (order == Sort.ASC || order == null) {
            expenseRepository.findAll().sortedBy { it.date }
        } else {
            expenseRepository.findAll().sortedByDescending { it.date }
        }
    }
}