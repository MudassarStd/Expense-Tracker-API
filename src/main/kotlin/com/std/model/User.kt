package com.std.model

import jakarta.persistence.*

@Entity
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = false)
    val email: String,
    @Column(nullable = false)
    val name: String,
    @Column(nullable = false)
    val password: String,

    // Understand following code
//    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
//    val expense: List<Expense> = emptyList()
)
