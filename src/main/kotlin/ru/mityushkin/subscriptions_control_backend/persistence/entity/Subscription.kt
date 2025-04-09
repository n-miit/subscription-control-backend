package ru.mityushkin.subscriptions_control_backend.persistence.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import ru.mityushkin.subscriptions_control_backend.persistence.entity.base.BaseAuditSoftDeleteEntity
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "subscriptions")
class Subscription(
    @ManyToOne
    @JoinColumn(name = "telegram_user_id")
    val telegramUser: TelegramUser,

    var serviceName: String,

    var amount: BigDecimal,

    @Enumerated(EnumType.STRING)
    var currency: Currency? = null,

    var billingDate: LocalDate,
) : BaseAuditSoftDeleteEntity() {

    enum class Currency {
        RUB, USD, EUR
    }

}