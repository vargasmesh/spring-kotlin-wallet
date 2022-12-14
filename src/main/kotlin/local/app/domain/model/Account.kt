package local.app.domain.model

typealias AccountID = String

data class CreateAccountEvent(val requestID: String, val owner: String)

enum class AccountEvents {
    ACCOUNT_CREATED,
    ACCOUNT_SUMMARY
}