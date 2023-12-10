package domain

@JvmInline
value class Id(val value: String)

fun String.toId() = Id(this)