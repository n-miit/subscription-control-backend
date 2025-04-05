package ru.mityushkin.subscriptions_control_backend.telegram

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException


class TelegramBot(
    @Value("\${telegram.token}")
    private val botToken: String,
) : TelegramLongPollingCommandBot(botToken) {
    override fun getBotUsername(): String {
        TODO("Not yet implemented")
    }

    override fun processNonCommandUpdate(update: Update?) {
        val msg = update!!.message
        val chatId: Long = msg.chatId
        val userName: String = getUserName(msg)

        setAnswer(chatId, userName, "answer")
    }

    /**
     * Formation of username
     * @param msg message
     */
    private fun getUserName(msg: Message): String {
        val user = msg.from
        val userName: String = user.userName
        return userName
    }

    /**
     * Send answer to Telegram
     * @param chatId
     * @param userName
     * @param text response text
     */
    private fun setAnswer(chatId: Long, userName: String, text: String) {
        val answer = SendMessage()
        answer.text = text
        answer.chatId = chatId.toString()
        try {
            execute(answer)
        } catch (e: TelegramApiException) {
            LOG.error("Error while sending telegram answer for user: $userName, error: ${e.message}")
        }
    }

    companion object {
        private val LOG = KotlinLogging.logger { }
    }
}