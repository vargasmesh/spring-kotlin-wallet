package local.app.pkg

import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import java.util.*


fun getRequestIDFromContext(): String {
    val requestID = RequestContextHolder
        .getRequestAttributes()
        ?.getAttribute("requestID", RequestAttributes.SCOPE_REQUEST)

    return requestID?.toString() ?: UUID.randomUUID().toString()
}