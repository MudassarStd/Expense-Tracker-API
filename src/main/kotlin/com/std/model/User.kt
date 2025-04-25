package com.std.model

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = false)
    val email: String,
    @Column(nullable = false)
    val name: String,
    @Column(nullable = false)
    private val password: String,

//    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
//    val expenses: MutableList<Expense> = mutableListOf()

    // Understand following code
//    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
//    val expense: List<Expense> = emptyList()
) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> = emptyList()

    override fun getPassword(): String = password

    override fun getUsername(): String = name

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}
