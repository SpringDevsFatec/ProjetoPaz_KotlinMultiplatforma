package com.projetopaz.kotlin.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Lazy
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val tokenService: TokenService,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    private val logger = LoggerFactory.getLogger(JwtAuthFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        if (!authHeader.isNullOrBlank() && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)
                print("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
            try {
                if (tokenService.isTokenValid(token)) {
                    val username = tokenService.extractUsername(token)
                    if (!username.isNullOrBlank() && SecurityContextHolder.getContext().authentication == null) {
                        val userDetails: UserDetails = userDetailsService.loadUserByUsername(username)
                        val authToken = UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.authorities
                        )

                        print("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                        SecurityContextHolder.getContext().authentication = authToken
                        print("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                    }
                }
            } catch (ex: Exception) {
                logger.warn("JWT validation failed: ${ex.message}")
                print("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB")
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.writer.write("Token inválido ou expirado")
                return
            }
        }

        println("SecurityContextHolder antes do chain: ${SecurityContextHolder.getContext().authentication}")
        filterChain.doFilter(request, response)
        println("SecurityContextHolder depois do chain: ${SecurityContextHolder.getContext().authentication}")


    }
}
