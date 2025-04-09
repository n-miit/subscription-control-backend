package ru.mityushkin.subscriptions_control_backend.service.manager

import org.springframework.stereotype.Component
import ru.mityushkin.subscriptions_control_backend.service.dto.SubscriptionDialogState
import ru.mityushkin.subscriptions_control_backend.service.dto.SubscriptionUserDialogState
import java.util.concurrent.ConcurrentHashMap

@Component
class SubscriptionDialogStateManager {
    private val userStates = ConcurrentHashMap<String, SubscriptionUserDialogState>()

    fun startSubscriptionDialog(chatId: String) {
        userStates[chatId] = SubscriptionUserDialogState(SubscriptionDialogState.AWAITING_SERVICE_NAME)
    }

    fun getState(chatId: String): SubscriptionUserDialogState? = userStates[chatId]

    fun completeDialog(chatId: String) {
        userStates.remove(chatId)
    }
}