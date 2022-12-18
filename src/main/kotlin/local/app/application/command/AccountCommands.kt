package local.app.application.command

import jakarta.transaction.Transactional
import local.app.domain.model.*
import org.springframework.stereotype.Service

sealed class DebitError {
    object InsufficientFunds : DebitError()
}

interface AccountRepository {
    fun createAccount(event: CreateAccountEvent): AccountID
    fun addToBalance(event: CreditAccountEvent)
    fun debitAccount(event: DebitAccountEvent)
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

    @Transactional
    fun debitAccount(event: DebitAccountEvent): DebitError? {
        if (!canDebit(event.account, event.amount)) return DebitError.InsufficientFunds
        accountRepository.debitAccount(event)
        return null
    }
}
