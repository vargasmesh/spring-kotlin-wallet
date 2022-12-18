package local.app.application.command

import jakarta.transaction.Transactional
import local.app.domain.model.AccountID
import local.app.domain.model.CreateAccountEvent
import local.app.domain.model.CreditAccountEvent
import org.springframework.stereotype.Service

interface AccountRepository {
    fun createAccount(event: CreateAccountEvent): AccountID
    fun addToBalance(event: CreditAccountEvent)
}

@Service
class AccountCommands(
    private val accountRepository: AccountRepository
) {

    @Transactional
    fun createAccount(event: CreateAccountEvent): AccountID {
        return accountRepository.createAccount(event)
    }

    @Transactional
    fun creditAccount(event: CreditAccountEvent) {
        accountRepository.addToBalance(event)
    }
}