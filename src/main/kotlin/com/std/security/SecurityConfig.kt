package com.std.security

import com.std.util.UserRole
import org.apache.catalina.webresources.TomcatURLStreamHandlerFactory.disable
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    private val logger = LoggerFactory.getLogger(SecurityConfig::class.java)

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun userDetailsService(): UserDetailsService {
        val user = User.withUsername("user")
            .password(passwordEncoder().encode("123456"))
//            .roles(UserRole.USER.toString())
            .build()

        logger.info("User created: $user")
        return InMemoryUserDetailsManager(user)
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { disable() }
            .authorizeHttpRequests {
                it.anyRequest().authenticated()
            }
            .httpBasic {  }
        return http.build()
    }
}