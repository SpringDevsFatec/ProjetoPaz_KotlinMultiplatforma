package com.projetopaz.kotlin.log

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.core.annotation.Order
import org.springframework.core.Ordered
import org.springframework.web.util.ContentCachingResponseWrapper

@Order(Ordered.LOWEST_PRECEDENCE)
//@Component
class RequestLoggingFilter : OncePerRequestFilter() {

    private val logger = LoggerFactory.getLogger(RequestLoggingFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val cachedRequest = ContentCachingRequestWrapper(request)
        val cachedResponse = ContentCachingResponseWrapper(response)

        try {
            filterChain.doFilter(cachedRequest, cachedResponse)
        } finally {
            logRequestBody(cachedRequest)
            cachedResponse.copyBodyToResponse()
        }
    }

    private fun logRequestBody(request: ContentCachingRequestWrapper) {
        val body = request.contentAsByteArray
        if (body.isNotEmpty()) {
            val bodyString = String(body, Charsets.UTF_8)
            logger.info("📩 JSON recebido: $bodyString")
        }
    }
}
