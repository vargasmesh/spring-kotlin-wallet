package local.app.pkg

import local.app.domain.model.currency
import org.javamoney.moneta.FastMoney

fun stringToFastMoney(amount: String): FastMoney {
    return FastMoney.of(amount.toDouble(), currency)
}