package local.app.adapter.db

import com.google.gson.Gson
import jakarta.persistence.*
import local.app.application.command.AccountRepository
import local.app.application.query.AccountEventsRepository
import local.app.domain.model.*
import local.app.pkg.getRequestIDFromContext
import local.app.pkg.stringToFastMoney
import org.javamoney.moneta.FastMoney
import org.springframework.stereotype.Repository
import java.util.UUID

data class AccountCreatedEventData(val request_id: String, val owner: String)
data class AccountCreditedEventData(val request_id: String, val amount: String)
data class AccountDebitedEventData(val request_id: String, val amount: String)
data class AccountSummaryEventData(val owner: String, val balance: String)


@Repository
class AccountRepositoryImpl(
    @PersistenceContext
    val entityManager: EntityManager
): AccountRepository, AccountEventsRepository {

    override fun createAccount(event: CreateAccountEvent): AccountID {
        val requestID = getRequestIDFromContext()

        val gson = Gson()
        val accountID = UUID.randomUUID().toString()
        val accountCreatedEvent = Event(
            type = AccountEventTypes.ACCOUNT_CREATED.name,
            entity_id = accountID,
            data = gson.toJson(AccountCreatedEventData(requestID, event.owner)),
        )

        val summaryEvent = Event(
            type = AccountEventTypes.ACCOUNT_SUMMARY.name,
            entity_id = accountID,
            data = gson.toJson(AccountSummaryEventData(event.owner, "0.00")),
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

    override fun addToBalance(event: CreditAccountEvent) {
        val gson = Gson()

        val requestID = getRequestIDFromContext()

        val creditedEvent = Event(
            type = AccountEventTypes.ACCOUNT_CREDITED.name,
            entity_id = event.accountID,
            data = gson.toJson(AccountCreditedEventData(requestID, event.amount.number.toString())),
        )

        entityManager.persist(creditedEvent)
    }

    override fun debitAccount(event: DebitAccountEvent) {
        val gson = Gson()

        val requestID = getRequestIDFromContext()

        val creditedEvent = Event(
            type = AccountEventTypes.ACCOUNT_DEBITED.name,
            entity_id = event.account.id,
            data = gson.toJson(AccountDebitedEventData(requestID, event.amount.number.toString())),
        )

        entityManager.persist(creditedEvent)
    }

    override fun getAccountEvents(accountID: AccountID): List<AccountEvent> {
        val account = entityManager.find(Account::class.java, accountID)
        val gson = Gson()

        return entityManager
            .createQuery("FROM Event e WHERE e.id >= :id AND e.entity_id = :entity_id", Event::class.java)
            .setParameter("id", account.last_summary_event.id)
            .setParameter("entity_id", account.id)
            .resultList
            .mapNotNull {
                when(it.type) {
                    AccountEventTypes.ACCOUNT_SUMMARY.name -> {
                        val data=gson.fromJson(it.data, AccountSummaryEventData::class.java)
                        AccountSummaryEvent(
                            account.id,
                            data.owner,
                            stringToFastMoney(data.balance)
                        )
                    }
                    AccountEventTypes.ACCOUNT_CREDITED.name -> {
                        val data=gson.fromJson(it.data, AccountCreditedEventData::class.java)
                        AccountCreditEvent(
                            account.id,
                            stringToFastMoney(data.amount)
                        )
                    }
                    AccountEventTypes.ACCOUNT_DEBITED.name -> {
                        val data=gson.fromJson(it.data, AccountDebitedEventData::class.java)
                        AccountDebitEvent(
                            account.id,
                            stringToFastMoney(data.amount)
                        )
                    }
                    else -> null
                }
            }
    }
}