package com.std.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.security.AuthProvider


@Configuration
class AppConfig {

    @Bean
    fun getBCryptPasswordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authManager(config: AuthenticationConfiguration): AuthenticationManager = config.authenticationManager

}