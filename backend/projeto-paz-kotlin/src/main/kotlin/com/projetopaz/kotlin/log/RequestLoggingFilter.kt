package com.projetopaz.kotlin.log

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper

@Component
class RequestLoggingFilter : OncePerRequestFilter() {

    private val logger = LoggerFactory.getLogger(RequestLoggingFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val wrappedRequest = ContentCachingRequestWrapper(request)
        filterChain.doFilter(wrappedRequest, response)

        val body = wrappedRequest.contentAsByteArray
        if (body.isNotEmpty()) {
            val bodyString = String(body, Charsets.UTF_8)
            logger.info("📩 JSON recebido: $bodyString")
        }
    }
}
