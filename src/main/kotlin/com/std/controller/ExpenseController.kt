package com.std.controller

import com.std.dto.ExpenseRequest
import com.std.dto.ExpenseResponse
import com.std.exception.InvalidRequestException
import com.std.model.Category
import com.std.model.Expense
import com.std.model.Type
import com.std.model.User
import com.std.service.ExpenseService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import kotlin.math.exp
import kotlin.math.log

@RestController
@RequestMapping("/expense")
class ExpenseController(private val expenseService: ExpenseService) {

    private val logger = LoggerFactory.getLogger(ExpenseController::class.java)

    @GetMapping("find/{id}")
    fun findById(
        @PathVariable(required = true) id: Long
    ): ExpenseResponse = expenseService.findById(id)

    @GetMapping
    fun getAll(
        @RequestParam(required = false) minAmount: Double?,
        @RequestParam(required = false) maxAmount: Double?,
        @RequestParam(required = false) category: Category?,
        @RequestParam(required = false) type: Type?,
        @RequestParam(required = false) startDate: LocalDate?,
        @RequestParam(required = false) endDate: LocalDate?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "5") pageSize: Int,

    ) = expenseService.findAllFiltered(minAmount, maxAmount, category, type, startDate, endDate, page, pageSize)

    @PostMapping
    fun add(
        @Valid @RequestBody(required = true) expenseRequest: ExpenseRequest
    ) = expenseService.add(expenseRequest)

    @PostMapping("/list")
    fun addList(
        @RequestBody(required = true) list: List<ExpenseRequest>
    ) {
        logger.debug("Total items in request body: ${list.size}")
        expenseService.addList(list)
    }

    @PutMapping("/update/{id}")
    fun update(
        @PathVariable(required = true) id: Long,
        @RequestBody(required = true) request: ExpenseRequest
    ) = expenseService.update(id, request)

    @DeleteMapping("/delete/{id}")
    fun delete(
        @PathVariable(required = true) id: Long
    ) = expenseService.deleteById(id)

    @GetMapping("/total")
    fun getTotalAmount(
        @RequestParam startDate: LocalDate?,
        @RequestParam endDate: LocalDate?
    ) = expenseService.getTotalAmount(startDate, endDate)

    @GetMapping("/total/type")
    fun getTotalAmountByType(
        @RequestParam(required = true) type: Type,
    ) = expenseService.getTotalAmountByType(type)

    @RequestMapping("get/sorted")
    fun getAllSorted(
        @RequestParam(required = false) order: Sort?
    ) = expenseService.findSortedByOrder(order)
}

enum class Sort { ASC, DEC }