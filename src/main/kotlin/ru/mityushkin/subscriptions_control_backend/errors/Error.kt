package ru.mityushkin.subscriptions_control_backend.errors

open class BadRequestExceptions(override val message: String?) : RuntimeException(message)

class IncorrectDateLessNow(val userName: String): BadRequestExceptions("The selected date is less than the current. username: $userName")