package domain

import java.util.UUID

interface UUIDGenerator {
    fun get(): String
}

class RandomUUIDGenerator: UUIDGenerator {
    override fun get(): String = UUID.randomUUID().toString()
}