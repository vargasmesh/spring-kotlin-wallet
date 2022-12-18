package local.app.application.query

import local.app.domain.model.Account
import local.app.domain.model.AccountEvent
import local.app.domain.model.AccountID
import local.app.domain.model.StateMachine
import org.springframework.stereotype.Service

interface AccountEventsRepository {
    fun getAccountEvents(accountID: AccountID): List<AccountEvent>
}

@Service
class AccountQueries(
    val accountEventsRepository: AccountEventsRepository
) {

    fun getAccount(accountID: AccountID): Account {
        return accountEventsRepository.getAccountEvents(accountID).fold(Account(accountID)) {
            acc, event -> StateMachine.update(acc, event)
        }
    }
}
