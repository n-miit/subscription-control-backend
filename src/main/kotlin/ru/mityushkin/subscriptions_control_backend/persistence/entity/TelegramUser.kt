package ru.mityushkin.subscriptions_control_backend.persistence.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.BatchSize
import ru.mityushkin.subscriptions_control_backend.persistence.entity.base.BaseAuditSoftDeleteEntity

@Entity
@Table(name = "telegram_users")
class TelegramUser(
    val chatId: String,
    val username: String,

    @OneToMany(
        mappedBy = "telegramUser",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL]
    )
    @BatchSize(size = 1000)
    val subscriptions: MutableList<Subscription> = mutableListOf(),
) : BaseAuditSoftDeleteEntity() {
}