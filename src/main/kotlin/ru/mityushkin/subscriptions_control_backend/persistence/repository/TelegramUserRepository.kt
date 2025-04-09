package ru.mityushkin.subscriptions_control_backend.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.mityushkin.subscriptions_control_backend.persistence.entity.TelegramUser

@Repository
interface TelegramUserRepository: JpaRepository<TelegramUser, Long> {

    fun findByUsername(username: String): TelegramUser?

    fun findByChatId(chatId: String): TelegramUser
}