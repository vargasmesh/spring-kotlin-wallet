package local.app.domain.model

import org.javamoney.moneta.FastMoney
import javax.money.CurrencyUnit
import javax.money.Monetary

typealias AccountID = String

val currency: CurrencyUnit = Monetary.getCurrency("USD")

data class Account(val id: AccountID, val balance: FastMoney = FastMoney.zero(currency), val owner: String = "")

sealed class AccountEvent

data class AccountSummaryEvent(val accountID: AccountID, val owner: String, val balance: FastMoney): AccountEvent()

data class CreateAccountEvent(val owner: String)

data class CreditAccountEvent(val accountID: AccountID, val amount: FastMoney)

enum class AccountEventTypes {
    ACCOUNT_CREATED,
    ACCOUNT_SUMMARY,
    ACCOUNT_CREDITED,
}