package domain

@JvmInline
value class Name(val value: String)

data class Customer(
    val id: Id,
    val name: Name,
    val age: Int,
    val favouriteDestinations: FavouriteDestinations
)

data class FavouriteDestinations(
    val destinations: List<Destination>
)

data class Destination(
    val city: String
)

fun String.toName() = Name(this)
