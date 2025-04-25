package com.std.config

import com.std.service.CustomUserDetailsService
import com.std.service.JwtFilter
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig (
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val userDetailsService: CustomUserDetailsService,
    private val jwtAuthFilter: JwtFilter
) {
    private val logger = LoggerFactory.getLogger(SecurityConfig::class.java)

//    @Bean
//    fun userDetailsService(): UserDetailsService {
//        val user = User.withUsername("user")
//            .password(bCryptPasswordEncoder.encode("123456"))
////            .roles(UserRole.USER.toString())
//            .build()
//
//        logger.info("User created: $user")
//        return InMemoryUserDetailsManager(user)
//    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it.requestMatchers("/expense/**").authenticated()
                    .anyRequest().permitAll()
            }
            .userDetailsService(userDetailsService)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }
}