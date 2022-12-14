package local.app.application.command

import local.app.domain.model.AccountID
import local.app.domain.model.CreateAccountEvent
import org.springframework.stereotype.Service

interface AccountRepository {
    fun createAccount(event: CreateAccountEvent): AccountID
}

@Service
class AccountService(
    private val accountRepository: AccountRepository
) {
    fun createAccount(event: CreateAccountEvent): AccountID {
        return accountRepository.createAccount(event)
    }
}