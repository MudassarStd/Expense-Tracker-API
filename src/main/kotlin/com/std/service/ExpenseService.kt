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
class ExpenseService(private val expenseRepository: ExpenseRepository) {

    private val logger = LoggerFactory.getLogger(ExpenseService::class.java) // representing KClass, then converting to Java
    
    fun findById(id: Long): ExpenseResponse {
        val expense = expenseRepository.findById(id).orElseThrow { ResourceNotFoundException("No expense found for id $id") }
        if (expense.user.id != getCurrentAuthenticatedUser().id) throw ResourceNotFoundException("Access Denied")
        return expense.toExpenseResponse()
    }

    fun add(expenseRequest: ExpenseRequest) {
        if (expenseRequest.amount <= 0) throw InvalidRequestException("Expense amount cannot be zero or less")
        expenseRepository.save(expenseRequest.toExpense(getCurrentAuthenticatedUser()))
    }

    fun addList(list: List<ExpenseRequest>) {
        logger.info("Request list size: ${list.size}")
        if (list.isEmpty()) throw InvalidRequestException("List cannot be empty")

        list.forEachIndexed { i, expense->
            if (expense.amount <= 0) throw InvalidRequestException("Expense amount is zero or less at list index $i")
        }

        val expenses = list.map { it.toExpense(getCurrentAuthenticatedUser()) }
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
        val pageResult: Page<Expense> = expenseRepository.findAll(pageRequest)

        val filteredExpenses = pageResult.content.filter { expense ->
            (expense.user == getCurrentAuthenticatedUser()) &&
            (minAmount == null || expense.amount >= minAmount) &&
                    (maxAmount == null || expense.amount <= maxAmount) &&
                    (category == null || expense.category == category) &&
                    (type == null || expense.type == type) && (startDate == null || expense.date >= startDate) && (endDate == null || expense.date <= endDate)
        }.map {
            it.toExpenseResponse()
        }

        return PaginatedResponse(
            expenses = filteredExpenses,
            hasNextPage = pageResult.hasNext()
        )
    }


    // fix update logic

//    fun update(id: Long, expense: Expense): Expense {
//        return if (validateUserExpense(id)) {
//            expenseRepository.save(findById(id).copy(id = id))
//        } else {
//            logger.info("Update Id: $id")
//            throw ResourceNotFoundException("No expense found for id $id")
//        }
//    }

    fun deleteById(id: Long): String {
        return if (validateUserExpense(id)) {
            expenseRepository.deleteById(id)
            "Deleted successfully"
        } else {
            throw ResourceNotFoundException("No expense found for id $id")
        }
    }

    fun getTotalAmount(
        startDate: LocalDate?,
        endDate: LocalDate?
    ): Double {
        logger.info("Date params from request, startDate: $startDate && endDate: $endDate")
        return expenseRepository.findAll()
            .filter { (it.user == getCurrentAuthenticatedUser()) && (startDate == null || it.date >= startDate) && (endDate == null || it.date <= endDate) }
            .sumOf { it.amount }
    }

    fun getTotalAmountByType(type: Type) = expenseRepository.findAll().filter {  it.type == type }.sumOf { it.amount }

    fun findSortedByOrder(order: Sort?): List<Expense> {
        return if (order == Sort.ASC || order == null) {
            expenseRepository.findAll().filter { it.user == getCurrentAuthenticatedUser()  }.sortedBy { it.date }
        } else {
            expenseRepository.findAll().filter{ it.user == getCurrentAuthenticatedUser() }. sortedByDescending { it.date }
        }
    }

    private fun validateUserExpense(eId: Long): Boolean {
        val expense = expenseRepository.findById(eId).orElseThrow { ResourceNotFoundException("") }
        return expense.user.id == getCurrentAuthenticatedUser().id
    }

    private fun getCurrentAuthenticatedUser(): User {
        return SecurityContextHolder.getContext().authentication.principal as User
    }
}