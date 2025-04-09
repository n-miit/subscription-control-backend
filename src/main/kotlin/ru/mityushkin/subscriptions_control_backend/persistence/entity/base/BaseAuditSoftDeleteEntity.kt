package ru.mityushkin.subscriptions_control_backend.persistence.entity.base

import jakarta.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseAuditSoftDeleteEntity : BaseAuditEntity() {
    var isDeleted: Boolean = false
}