//package com.std.config
//
//import org.springframework.beans.factory.annotation.Configurable
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.http.HttpMethod
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import org.springframework.security.config.http.SessionCreationPolicy
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
//import org.springframework.security.crypto.password.PasswordEncoder
//import org.springframework.security.web.SecurityFilterChain
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher
//
//@Configuration
//class Config {
//
//    // we tell IOC container that how to provide this object if it is required as dependency somewhere
//    @Bean
//    fun passwordEncoder(): BCryptPasswordEncoder {
//        return BCryptPasswordEncoder()
//    }
//}
//
//
////@Configuration
////@EnableWebSecurity
////class SecurityConfiguration {
////
////    @Bean
////    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
////        http
////            .authorizeHttpRequests { requests ->
////                requests
////                    // Match POST to /api/auth/register (with or without trailing slash)
////                    .requestMatchers(
////                        AntPathRequestMatcher("/api/auth/register", "POST"),
////                        AntPathRequestMatcher("/api/auth/register/", "POST")
////                    ).permitAll()
////                    .anyRequest().authenticated()
////            }
////            .csrf { csrf -> csrf.disable() }
////            .httpBasic { it.realmName("Realm") }
////            // Disable session creation
////            .sessionManagement {
////                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////            }
////
////        return http.build()
////    }
////}