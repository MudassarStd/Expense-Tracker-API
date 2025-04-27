package com.std.service

import com.std.controller.Sort
import com.std.dto.ExpenseRequest
import com.std.dto.ExpenseResponse
import com.std.dto.PaginatedResponse
import com.std.exception.InvalidRequestException
import com.std.exception.ResourceNotFoundException
import com.std.mapper.toExpense
import com.std.mapper.toExpenseResponse
import com.std.model.Category
import com.std.model.Expense
import com.std.model.Type
import com.std.model.User
import com.std.repository.ExpenseRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.util.Streamable
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException.Unauthorized
import java.time.LocalDate
import kotlin.math.exp

@Service
class ExpenseService(
    private val expenseRepository: ExpenseRepository,
    private val authService: AuthService
) {

    private val logger =
        LoggerFactory.getLogger(ExpenseService::class.java) // representing KClass, then converting to Java

    fun findById(id: Long): ExpenseResponse {
        val expense = expenseRepository.findByIdAndUser(id, authService.getCurrentAuthenticatedUser())
            ?: throw ResourceNotFoundException("No expense found for id $id")
        return expense.toExpenseResponse()
    }

    fun add(expenseRequest: ExpenseRequest) {
        if (expenseRequest.amount <= 0) throw InvalidRequestException("Expense amount cannot be zero or less")
        expenseRepository.save(expenseRequest.toExpense(authService.getCurrentAuthenticatedUser()))
    }

    fun addList(list: List<ExpenseRequest>) {
        logger.info("Request list size: ${list.size}")
        if (list.isEmpty()) throw InvalidRequestException("List cannot be empty")
        list.forEachIndexed { i, expense ->
            if (expense.amount <= 0) throw InvalidRequestException("Expense amount is zero or less at list index $i")
        }
        val expenses = list.map { it.toExpense(authService.getCurrentAuthenticatedUser()) }
        expenseRepository.saveAll(expenses)
    }

    fun findAllFiltered(
        minAmount: Double?,
        maxAmount: Double?,
        category: Category?,
        type: Type?,
        startDate: LocalDate?,
        endDate: LocalDate?,
        page: Int,
        pageSize: Int
    ): PaginatedResponse {

        val pageRequest = PageRequest.of(page, pageSize)
        val pageResult: Page<ExpenseResponse> =
            expenseRepository.findAllByUser(authService.getCurrentAuthenticatedUser(), pageRequest)

        val filteredExpenses = pageResult.content.filter { expense ->
            (minAmount == null || expense.amount >= minAmount) &&
                    (maxAmount == null || expense.amount <= maxAmount) &&
                    (category == null || expense.category == category) &&
                    (type == null || expense.type == type) && (startDate == null || expense.date >= startDate) && (endDate == null || expense.date <= endDate)
        }

        return PaginatedResponse(
            expenses = filteredExpenses,
            hasNextPage = pageResult.hasNext()
        )
    }

    fun update(id: Long, request: ExpenseRequest): String {
        expenseRepository.findByIdAndUser(id, authService.getCurrentAuthenticatedUser())?.let{ expense ->
            expenseRepository.save(expense.copy(
            amount = request.amount,
            date = request.date,
            type = request.type,
            category = request.category,
        ))
        } ?: throw ResourceNotFoundException("No expense found for id $id")
        return "Updated successfully"
    }

    fun deleteById(id: Long): String {
        expenseRepository.findByIdAndUser(id, authService.getCurrentAuthenticatedUser())?.let { expense ->
            expenseRepository.delete(expense)
        } ?: throw ResourceNotFoundException("No expense found for id $id")
        return "Deleted successfully"
    }

    fun getTotalAmount(
        startDate: LocalDate?,
        endDate: LocalDate?
    ): Double {
        logger.info("Date params from request, startDate: $startDate && endDate: $endDate")
        return expenseRepository.findAllByUser(authService.getCurrentAuthenticatedUser())
            .filter { ((startDate == null || it.date >= startDate) && (endDate == null || it.date <= endDate)) }
            .sumOf { it.amount }
    }

    fun getTotalAmountByType(type: Type) =
        expenseRepository.findAllByUser(authService.getCurrentAuthenticatedUser()).filter { it.type == type }.sumOf { it.amount }

    fun findSortedByOrder(order: Sort?): List<ExpenseResponse> {
        return if (order == Sort.ASC || order == null) {
            expenseRepository.findAllByUser(authService.getCurrentAuthenticatedUser()).sortedBy { it.date }
        } else {
            expenseRepository.findAllByUser(authService.getCurrentAuthenticatedUser()).sortedByDescending { it.date }
        }
    }
}