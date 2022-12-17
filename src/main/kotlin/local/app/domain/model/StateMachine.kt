package local.app.domain.model

class StateMachine {
    companion object {
        fun update(`_`: Account, event: AccountSummaryEvent): Account {
            return Account(
                id = event.accountID,
                owner = event.owner,
                balance = event.balance
            )
        }
    }
}