package local.app.adapter.graphql

import local.app.application.command.AccountCommands
import local.app.domain.model.AccountID
import local.app.domain.model.CreateAccountEvent
import local.app.domain.model.CreditAccountEvent
import local.app.domain.model.currency
import local.app.pkg.getRequestIDFromContext
import org.javamoney.moneta.FastMoney
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller


@Controller
class AccountResolver(
    @Autowired
    val accountCommands: AccountCommands
) {
    @QueryMapping
    fun hello(): String {
        return "world"
    }

    @MutationMapping
    fun createAccount (@Argument owner: String): AccountID {
        return accountCommands.createAccount(CreateAccountEvent(owner))
    }

    @MutationMapping
    fun creditAccount(@Argument accountID: String, @Argument amount: String): String {
        accountCommands.creditAccount(
            CreditAccountEvent(
                accountID,
                FastMoney.of(amount.toDouble(), currency) ,
            )
        )

        return getRequestIDFromContext()
    }
}