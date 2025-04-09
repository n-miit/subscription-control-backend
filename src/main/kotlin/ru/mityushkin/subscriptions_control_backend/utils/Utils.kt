package ru.mityushkin.subscriptions_control_backend.utils

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import ru.mityushkin.subscriptions_control_backend.persistence.entity.Subscription
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Service
class Utils {

    fun getUserName(msg: Message): String {
        return getUserName(msg.from)
    }

    fun getUserName(user: User): String {
        return if (user.userName != null) {
            user.userName
        } else {
            user.lastName + " " + user.firstName
        }
    }
}

fun List<Subscription>.formatToUserFriendlyString(): String {
    if (this.isEmpty()) return "У вас пока нет активных подписок ❌"

    val header = """
        *📊 Ваши подписки:*
        Всего: ${this.size} | Месячная сумма: ${this.sumOf { it.amount }} ${this.first().currency ?: "RUB"}
        ━━━━━━━━━━━━━━━━━
    """.trimIndent()

    return this.sortedBy { it.billingDate }
        .fold(header) { acc, subscription ->
            acc + """
 
 
                *🔹 ${subscription.serviceName}*
                💰 ${subscription.amount} ${subscription.currency ?: "RUB"}
                📅 ${subscription.billingDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}
                ⏳ Осталось: ${ChronoUnit.DAYS.between(LocalDate.now(), subscription.billingDate)} дн.
            """.trimIndent()
        }
}