package local.app.adapter.graphql

import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.UUID

@Component
class RequestIDInterceptor: WebGraphQlInterceptor {
    override fun intercept(request: WebGraphQlRequest, chain: WebGraphQlInterceptor.Chain): Mono<WebGraphQlResponse> {
        request.configureExecutionInput { executionInput, builder ->
            builder.graphQLContext {
                it.put("requestID", UUID.randomUUID().toString())
            }.build()
        }
        return chain.next(request)
    }
}