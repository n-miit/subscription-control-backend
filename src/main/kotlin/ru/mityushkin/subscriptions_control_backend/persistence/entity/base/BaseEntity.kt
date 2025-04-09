package ru.mityushkin.subscriptions_control_backend.persistence.entity.base

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.springframework.data.util.ProxyUtils


@MappedSuperclass
abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    override fun equals(other: Any?): Boolean {
        other ?: return false;
        if (this === other) return true
        if (javaClass != ProxyUtils.getUserClass(other)) return false

        other as BaseEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int = 33

    override fun toString(): String {
        return "${this.javaClass.simpleName}(id=$id)"
    }
}