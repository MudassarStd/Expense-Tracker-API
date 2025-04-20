package com.std.service

import com.std.controller.Sort
import com.std.dto.ExpenseRequest
import com.std.exception.InvalidRequestException
import com.std.exception.ResourceNotFoundException
import com.std.mapper.toExpense
import com.std.model.Category
import com.std.model.Expense
import com.std.model.Type
import com.std.repository.ExpenseRepository
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.logging.Logger
import javax.swing.SortOrder
import kotlin.math.exp
import kotlin.math.log
import kotlin.math.min

@Service
class ExpenseService(private val expenseRepository: ExpenseRepository) {

    private val logger =
        LoggerFactory.getLogger(ExpenseService::class.java) // representing KClass, then converting to Java
    
    fun findById(id: Long): Expense =
        expenseRepository.findById(id).orElseThrow { ResourceNotFoundException("No expense found for id $id") }

    fun add(expenseRequest: ExpenseRequest) {
        if (expenseRequest.amount <= 0) throw InvalidRequestException("Expense amount cannot be zero or less")
        expenseRepository.save(expenseRequest.toExpense())
    }

    fun addList(list: List<ExpenseRequest>) {
        logger.info("Request list size: ${list.size}")
        if (list.isEmpty()) throw InvalidRequestException("List cannot be empty")

        list.forEachIndexed { i, expense->
            if (expense.amount <= 0) throw InvalidRequestException("Expense amount is zero or less at list index $i")
        }

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

    fun update(id: Long, expense: Expense): Expense {
        return if (expenseRepository.existsById(id)) {
            expenseRepository.save(expense.copy(id = id))
        } else if (id < 0) {
            throw InvalidRequestException("Invalid id $id")
        } else {
            logger.info("Update Id: $id")
            throw ResourceNotFoundException("No expense found for id $id")
        }
    }

    fun deleteById(id: Long): String {
        return if (expenseRepository.existsById(id)) {
            expenseRepository.deleteById(id)
            "Deleted successfully"
        } else if (id < 0) {
            logger.info("Delete id: $id")
            throw InvalidRequestException("Invalid id $id")
        } else {
            throw ResourceNotFoundException("No expense found for id $id")
        }
    }

    fun getTotalAmount(
        startDate: LocalDate?,
        endDate: LocalDate?
    ) {
        logger.info("Date params from request, startDate: $startDate && endDate: $endDate")
        expenseRepository.findAll()
            .filter { (startDate == null || it.date >= startDate) && (endDate == null || it.date <= endDate) }
            .sumOf { it.amount }
    }

    fun getTotalAmountByType(type: Type) = expenseRepository.findAll().filter { it.type == type }.sumOf { it.amount }

    fun findSortedByOrder(order: Sort?): List<Expense> {
        return if (order == Sort.ASC || order == null) {
            expenseRepository.findAll().sortedBy { it.date }
        } else {
            expenseRepository.findAll().sortedByDescending { it.date }
        }
    }
}