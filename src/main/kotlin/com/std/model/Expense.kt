package com.std.model

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
data class Expense(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val amount: Double,
    val description: String,
    @Enumerated(EnumType.STRING)
    val category: Category,
    @Enumerated(EnumType.STRING)
    val type: Type,
    val date: LocalDate = LocalDate.now()

    // Understand below code

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    val user: User
)

enum class Type { EXPENSE, INCOME }

enum class Category {
    FOOD,
    TRAVEL,
    UTILITIES,
    HEALTH,
    ENTERTAINMENT,
    OTHERS
}
