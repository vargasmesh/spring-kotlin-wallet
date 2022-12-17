package local.app.adapter.graphql

import local.app.application.command.AccountService
import local.app.domain.model.AccountID
import local.app.domain.model.CreateAccountEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.ContextValue
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller


@Controller
class AccountResolver(
    @Autowired
    val accountService: AccountService
) {
    @QueryMapping
    fun hello(): String {
        return "world"
    }

    @MutationMapping
    fun createAccount (@ContextValue requestID: String, @Argument owner: String): AccountID {
        return accountService.createAccount(CreateAccountEvent(requestID, owner))
    }
}