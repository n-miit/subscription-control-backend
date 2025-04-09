package ru.mityushkin.subscriptions_control_backend.service.commands

import mu.KotlinLogging
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.bots.AbsSender
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

abstract class ServiceCommand(identifier: String?, description: String?) : BotCommand(identifier, description) {

    fun sendAnswer(absSender: AbsSender, chatId: Long, commandName: String?, userName: String?, text: String) {
        val message = SendMessage()
        message.enableMarkdown(true)
        message.chatId = chatId.toString()
        message.text = text
        try {
            absSender.execute(message)
        } catch (e: TelegramApiException) {
            LOG.error{ "Error ${e.message}. Command $commandName. User: $userName" }
            e.printStackTrace()
        }
    }

    fun sendAnswer(absSender: AbsSender, commandName: String?, userName: String?, message: SendMessage) {
        try {
            absSender.execute(message)
        } catch (e: TelegramApiException) {
            LOG.error{ "Error ${e.message}. Command $commandName. User: $userName" }
            e.printStackTrace()
        }
    }

    companion object {
        val LOG = KotlinLogging.logger {}
    }
}