package ru.mityushkin.subscriptions_control_backend.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.mityushkin.subscriptions_control_backend.persistence.entity.Subscription

@Repository
interface SubscriptionRepository : JpaRepository<Subscription, Long> {

    @Query("""
        SELECT s FROM Subscription s
        JOIN TelegramUser tu on tu.id = s.telegramUser.id
        WHERE tu.username = :userName and tu.chatId = :chatId and s.isDeleted = false
    """)
    fun findAllByUsernameAndChatId(userName: String, chatId: String): List<Subscription>
}