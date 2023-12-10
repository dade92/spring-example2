package domain

data class Customer(
    val id: Id,
    val name: String,
    val age: Int,
    val favouriteDestinations: FavouriteDestinations
)

data class FavouriteDestinations(
    val destinations: List<Destination>
)

data class Destination(
    val city: String
)