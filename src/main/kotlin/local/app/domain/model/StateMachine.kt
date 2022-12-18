package local.app.domain.model

class StateMachine {
    companion object {
        fun update(account: Account, event: AccountEvent): Account {
            return when(event) {
                is AccountSummaryEvent -> {
                    Account(
                        id = event.accountID,
                        owner = event.owner,
                        balance = event.balance
                    )
                }
                is AccountCreditEvent -> {
                    Account(
                        id = account.id,
                        owner = account.owner,
                        balance =  account.balance.add(event.amount)
                    )
                }
                is AccountDebitEvent -> {
                    Account(
                        id = account.id,
                        owner = account.owner,
                        balance =  account.balance.subtract(event.amount)
                    )
                }
                else -> account
            }
        }
    }
}