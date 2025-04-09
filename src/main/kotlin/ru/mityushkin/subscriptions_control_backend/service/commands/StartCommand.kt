package ru.mityushkin.subscriptions_control_backend.service.commands

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.mityushkin.subscriptions_control_backend.persistence.entity.TelegramUser
import ru.mityushkin.subscriptions_control_backend.persistence.repository.TelegramUserRepository
import ru.mityushkin.subscriptions_control_backend.utils.getWelcomeMessage

/**
 * command /start
 */
@Service
class StartCommand(
    @Value("\${telegram.start.identifier}")
    identifier: String?,
    @Value("\${telegram.start.description}")
    description: String?,
    private val telegramUserRepository: TelegramUserRepository
) : ServiceCommand(identifier, description) {

    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<String>?) {
        val userName = telegramUserRepository.findByUsername(user.userName)
            ?: TelegramUser(
                chatId = chat.id.toString(),
                username = user.userName,
            )
                .also { telegramUserRepository.save(it) }

        LOG.debug{ "User $userName. Command execution started ${this.commandIdentifier}" }

        val keyboard = InlineKeyboardMarkup().apply {
            keyboard = listOf(
                listOf(
                    InlineKeyboardButton(addSubCommand).apply {
                        callbackData = "ADD_SUBSCRIPTION"
                    },
                    InlineKeyboardButton(getAllSub).apply {
                        callbackData = "GET_ALL_SUBSCRIPTION"
                    }
                )
            )
        }

        val message = SendMessage.builder()
            .chatId(chat.id.toString())
            .text(getWelcomeMessage())
            .parseMode(parseMod)
            .replyMarkup(keyboard)
            .build()

        sendAnswer(absSender, this.commandIdentifier, userName.username, message)
        LOG.debug{ "User $userName. Command execution completed ${this.commandIdentifier}" }
    }

    companion object {
        val LOG = KotlinLogging.logger {}
        val parseMod = "MarkdownV2"
        val addSubCommand = "Добавить подписку"
        val getAllSub = "Мои подписки"
    }
}