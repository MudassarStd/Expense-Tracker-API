package com.std.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class Expense(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val amount: Double,
    val description: String,
    @Enumerated(EnumType.STRING)
    val category: Category,

    // Understand below code

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    val user: User
)

enum class Category {
    GROCERIES,
    LEISURE,
    ELECTRONICS,
    UTILITIES,
    CLOTHING,
    HEALTH,
    OTHERS
}