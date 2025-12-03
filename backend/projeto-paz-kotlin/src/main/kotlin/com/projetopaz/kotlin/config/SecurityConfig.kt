package com.projetopaz.kotlin.config

import com.projetopaz.kotlin.security.JwtAuthFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.core.Ordered
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
@EnableMethodSecurity
class SecurityConfig(
    @Lazy private val jwtAuthFilter: JwtAuthFilter
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager =
        authConfig.authenticationManager

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // 👇 Aqui dizemos explicitamente qual configuração usar, evitando o erro de "Bean automático"
            .cors { it.configurationSource(corsConfigurationSource()) }
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                // Endpoints Públicos
                auth.requestMatchers("/api/auth/**").permitAll()
                auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                auth.requestMatchers("/error").permitAll()

                // 👇 LIBERAÇÃO DE IMAGENS
                auth.requestMatchers(HttpMethod.GET, "/api/product/img/**").permitAll()
                auth.requestMatchers(HttpMethod.GET, "/api/category/img/**").permitAll()

                // 👇 LIBERA O PREFLIGHT (OPTIONS)
                auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Restante bloqueado
                auth.anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    // 1. Definimos as regras de CORS em um lugar central
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOrigin("http://localhost:8080")
        config.addAllowedOrigin("http://127.0.0.1:8080")
        config.addAllowedOrigin("http://10.0.2.2:8081")
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }

    // 2. Criamos o Filtro Global com um nome diferente ("corsFilterRegistration") para não conflitar
    @Bean
    fun corsFilterRegistration(): FilterRegistrationBean<CorsFilter> {
        // Usamos a mesma configuração criada acima
        val source = corsConfigurationSource() as UrlBasedCorsConfigurationSource
        val bean = FilterRegistrationBean(CorsFilter(source))

        // Prioridade Máxima: roda antes de qualquer segurança
        bean.order = Ordered.HIGHEST_PRECEDENCE
        return bean
    }
}