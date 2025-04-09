package ru.mityushkin.subscriptions_control_backend.utils

fun getWelcomeMessage(): String = """
        Привет\! 👋 Я — твой персональный помощник по подпискам\.  

        **Что я умею:**  
        🔹 Напоминать о списании денег *за 1\-3 дня* до платежа\.  
        🔹 Показывать все активные подписки в одном месте\.  
        🔹 Считать, сколько ты тратишь в месяц/год\.  

        **Обратная связь**  
        Нашли баг или есть идея? Пиши сюда: [@n\_miit](https://t.me/n_miit) — мы всё улучшим\!
    """.trimIndent()