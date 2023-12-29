package domain

import java.time.LocalDateTime

interface TimeProvider {
    fun now(): LocalDateTime
}

class DefaultTimeProvider: TimeProvider {
    override fun now(): LocalDateTime = LocalDateTime.now()
}