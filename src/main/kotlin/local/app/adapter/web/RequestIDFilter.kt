package local.app.adapter.web


import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.filter.GenericFilterBean
import java.util.UUID

@Component
class RequestIDFilter: GenericFilterBean() {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        RequestContextHolder
            .getRequestAttributes()
            ?.setAttribute("requestID", UUID.randomUUID().toString(), RequestAttributes.SCOPE_REQUEST)
        chain.doFilter(request, response)
    }
}