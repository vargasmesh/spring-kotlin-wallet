package local.app.adapter.graphql

import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment
import local.app.application.command.AccountCommands
import local.app.application.command.DebitError
import local.app.application.query.AccountQueries
import local.app.domain.model.*
import org.javamoney.moneta.FastMoney
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter
import org.springframework.graphql.execution.ErrorType
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller

@Component
class AccountExceptionResolver: DataFetcherExceptionResolverAdapter() {
    override fun resolveToSingleError(ex: Throwable, env: DataFetchingEnvironment): GraphQLError? {
        return when(ex) {
            is InsufficientFunds -> GraphqlErrorBuilder.newError()
                .errorType(ErrorType.BAD_REQUEST)
                .message("insufficient funds")
                .build()
            else -> GraphqlErrorBuilder.newError()
                .errorType(ErrorType.INTERNAL_ERROR)
                .message("something went wrong")
                .build()
        }
    }
}

class InsufficientFunds(message: String = "insufficient funds"): RuntimeException()

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

        val error = accountCommands.debitAccount(DebitAccountEvent(account, FastMoney.of(amount.toDouble(), currency) ))
        return when(error) {
            is DebitError.InsufficientFunds -> throw InsufficientFunds()
            else -> accountQueries.getAccount(accountID)
        }
    }

    @MutationMapping
    fun createAccount (@Argument owner: String): AccountID {
        return accountCommands.createAccount(CreateAccountEvent(owner))
    }

    @MutationMapping
    fun creditAccount(@Argument accountID: String, @Argument amount: String): Account {
        accountCommands.creditAccount(
            CreditAccountEvent(
                accountID,
                FastMoney.of(amount.toDouble(), currency) ,
            )
        )

        return accountQueries.getAccount(accountID)
    }
}