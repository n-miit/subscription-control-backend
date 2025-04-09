package ru.mityushkin.subscriptions_control_backend.service

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.mityushkin.subscriptions_control_backend.errors.IncorrectDateLessNow
import ru.mityushkin.subscriptions_control_backend.persistence.entity.Subscription
import ru.mityushkin.subscriptions_control_backend.persistence.repository.SubscriptionRepository
import ru.mityushkin.subscriptions_control_backend.persistence.repository.TelegramUserRepository
import ru.mityushkin.subscriptions_control_backend.service.dto.SubscriptionDialogState
import ru.mityushkin.subscriptions_control_backend.service.dto.SubscriptionUserDialogState
import ru.mityushkin.subscriptions_control_backend.service.manager.SubscriptionDialogStateManager
import ru.mityushkin.subscriptions_control_backend.utils.formatToUserFriendlyString
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class SubscriptionService(
    private val subscriptionDialogStateManager: SubscriptionDialogStateManager,
    private val subscriptionRepository: SubscriptionRepository,
    private val telegramUserRepository: TelegramUserRepository,
) {
    fun handleAddSubscription(update: Update, bot: AbsSender) {
        val chatId = update.callbackQuery.message.chatId.toString()
        subscriptionDialogStateManager.startSubscriptionDialog(chatId)

        bot.execute(SendMessage(chatId, "Введите название сервиса (например: Netflix):"))
    }

    fun handleUserInput(update: Update, bot: AbsSender) {
        val chatId = update.message.chatId.toString()
        val state = subscriptionDialogStateManager.getState(chatId) ?: return

        when (state.currentState) {
            SubscriptionDialogState.AWAITING_SERVICE_NAME -> {
                state.serviceName = update.message.text
                state.currentState = SubscriptionDialogState.AWAITING_PRICE
                bot.execute(SendMessage(chatId, "Введите стоимость подписки (например: 599.00):"))
            }

            SubscriptionDialogState.AWAITING_PRICE -> {
                try {
                    state.amount = BigDecimal(update.message.text)
                    state.currentState = SubscriptionDialogState.AWAITING_DATE
                    bot.execute(SendMessage(chatId, "Введите дату платежа (в формате ДД.ММ.ГГГГ):"))
                } catch (e: Exception) {
                    bot.execute(SendMessage(chatId, "Неверный формат суммы. Попробуйте еще раз:"))
                }
            }

            SubscriptionDialogState.AWAITING_DATE -> {
                try {
                    val inputDate =
                    state.date =
                    saveSubscription(bot, chatId, state)
                    subscriptionDialogStateManager.completeDialog(chatId)
                } catch (e: Exception) {
                    bot.execute(SendMessage(chatId, "Неверный формат даты. Используйте ДД.ММ.ГГГГ:"))
                }
            }

            SubscriptionDialogState.COMPLETED -> Unit
        }
    }

    fun getAllSubscriptions(update: Update, bot: AbsSender) {
        val user = update.callbackQuery.from.userName
        val chatId = update.callbackQuery.message.chatId.toString()

        val allSubscriptions = subscriptionRepository.findAllByUsernameAndChatId(user, chatId)

        bot.execute(SendMessage(chatId, allSubscriptions.formatToUserFriendlyString()))
    }

    fun checkActiveDialog(chatId: String) : Boolean =
        subscriptionDialogStateManager.getState(chatId) != null

    private fun saveSubscription(bot: AbsSender, chatId: String, state: SubscriptionUserDialogState) {
        val user = telegramUserRepository.findByChatId(chatId)

        val subscription = Subscription(
            telegramUser = user,
            serviceName = requireNotNull(state.serviceName),
            amount = requireNotNull(state.amount),
            billingDate = requireNotNull(state.date)
        )
        subscriptionRepository.save(subscription)

        bot.execute(SendMessage(chatId,
            "Подписка сохранена!\n" +
                    "Сервис: ${state.serviceName}\n" +
                    "Стоимость: ${state.amount} руб.\n" +
                    "Дата платежа: ${state.date}"))
    }

    private fun checkValidDate(
        userName: String,
        date: LocalDate
    ) {
        val isValid = date.isAfter(LocalDate.now())

        if (!isValid) {
            throw IncorrectDateLessNow()
        }
    }
}