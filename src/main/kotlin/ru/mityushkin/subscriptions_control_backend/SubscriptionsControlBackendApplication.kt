package ru.mityushkin.subscriptions_control_backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import ru.mityushkin.subscriptions_control_backend.telegram.TelegramBot


@SpringBootApplication
class SubscriptionsControlBackendApplication

fun main(args: Array<String>) {
    val context = runApplication<SubscriptionsControlBackendApplication>(*args)

    try {
        val botsApi = TelegramBotsApi(DefaultBotSession::class.java)
        val bot = context.getBean(TelegramBot::class.java)
        botsApi.registerBot(bot)
    } catch (e: TelegramApiException) {
        e.printStackTrace()
    }
}
