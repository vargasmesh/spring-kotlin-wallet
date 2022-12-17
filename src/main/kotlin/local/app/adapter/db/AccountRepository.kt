package local.app.adapter.db

import com.google.gson.Gson
import jakarta.persistence.*
import local.app.application.command.AccountRepository
import local.app.domain.model.AccountEventTypes
import local.app.domain.model.AccountID
import local.app.domain.model.CreateAccountEvent
import org.springframework.stereotype.Repository
import java.util.UUID

data class AccountCreatedEvent(val request_id: String, val owner: String)
data class AccountSummaryEvent(val owner: String, val balance: String)


@Repository
class AccountRepositoryImpl(
    @PersistenceContext
    val entityManager: EntityManager
): AccountRepository  {

    override fun createAccount(event: CreateAccountEvent): AccountID {
        val gson = Gson()
        val accountID = UUID.randomUUID().toString()
        val accountCreatedEvent = Event(
            type = AccountEventTypes.ACCOUNT_CREATED.name,
            entity_id = accountID,
            data = gson.toJson(AccountCreatedEvent(event.requestID, event.owner)),
        )

        val summaryEvent = Event(
            type = AccountEventTypes.ACCOUNT_SUMMARY.name,
            entity_id = accountID,
            data = gson.toJson(AccountSummaryEvent(event.owner, "0.00")),
        )

        val account = Account(
            id = accountID,
            last_summary_event = summaryEvent,
        )

        entityManager.persist(accountCreatedEvent)
        entityManager.persist(summaryEvent)
        entityManager.persist(account)


        return accountID
    }
}