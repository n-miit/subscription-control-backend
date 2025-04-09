package ru.mityushkin.subscriptions_control_backend.service.dto

import java.math.BigDecimal
import java.time.LocalDate

enum class SubscriptionDialogState {
    AWAITING_SERVICE_NAME,
    AWAITING_PRICE,
    AWAITING_DATE,
    COMPLETED
}

data class SubscriptionUserDialogState(
    var currentState: SubscriptionDialogState = SubscriptionDialogState.COMPLETED,
    var serviceName: String? = null,
    var amount: BigDecimal? = null,
    var date: LocalDate? = null
)