package ru.mityushkin.subscriptions_control_backend.telegram

import jakarta.annotation.PostConstruct
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import ru.mityushkin.subscriptions_control_backend.service.SubscriptionService

@Service
class TelegramBot(
    @Value("\${telegram.token}")
    private val botToken: String = "",
    private val commandList: List<IBotCommand>,
    private val subscriptionService: SubscriptionService,
) : TelegramLongPollingCommandBot(botToken) {

    @PostConstruct
    fun init() {
        commandList.forEach { command ->
            register(command)
            LOG.debug { "Command registered: ${command.commandIdentifier}" }
        }
    }

    override fun getBotUsername(): String {
        return "PaidSubscriptionControlBot"
    }

    override fun processNonCommandUpdate(update: Update) {
        if (update.hasCallbackQuery()) {
            val chatId = update.callbackQuery.message.chatId.toString()
            val data = update.callbackQuery.data
            val userName = update.callbackQuery.from.userName


            when (data) {
                ADD_SUBSCRIPTION -> {
                    subscriptionService.handleAddSubscription(update, this)
                }
                GET_ALL_SUBSCRIPTION -> {
                    subscriptionService.getAllSubscriptions(update, this)
                }
                else -> {
                    setAnswer(chatId, userName, "unknown")
                }
            }
        } else {
            val msg = update.message
            val chatId: String = msg.chatId.toString()
            val userName: String = getUserName(msg)

            if (subscriptionService.checkActiveDialog(chatId)) {
                subscriptionService.handleUserInput(update, this)
            } else {
                setAnswer(chatId, userName, "unknown")
            }
        }
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
    private fun setAnswer(chatId: String, userName: String, text: String) {
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
        private const val ADD_SUBSCRIPTION = "ADD_SUBSCRIPTION"
        private const val GET_ALL_SUBSCRIPTION = "GET_ALL_SUBSCRIPTION"
    }
}