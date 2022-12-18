package local.app.adapter.graphql

import local.app.application.command.AccountCommands
import local.app.application.query.AccountQueries
import local.app.domain.model.*
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
    val accountCommands: AccountCommands,
    @Autowired
    val accountQueries: AccountQueries
) {
    @QueryMapping
    fun account(@Argument id: String): Account {
        return accountQueries.getAccount(id)
    }

    @MutationMapping
    fun debitAccount(@Argument accountID: String, @Argument amount: String): Account {
        val account = accountQueries.getAccount(accountID)
        accountCommands.debitAccount(DebitAccountEvent(account, FastMoney.of(amount.toDouble(), currency) ))
        return accountQueries.getAccount(accountID)
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