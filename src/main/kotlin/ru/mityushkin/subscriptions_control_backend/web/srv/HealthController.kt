package ru.mityushkin.subscriptions_control_backend.web.srv

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {
    @GetMapping("/health")
    fun health() = "OK"
}