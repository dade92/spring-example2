package domain

import java.time.LocalDateTime
import java.time.ZoneId

interface TimeProvider {
    fun now(): LocalDateTime
}

class DefaultTimeProvider: TimeProvider {
    override fun now(): LocalDateTime = LocalDateTime.now(ZoneId.of("Europe/Rome"))
}