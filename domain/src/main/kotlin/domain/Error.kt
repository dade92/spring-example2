package domain

sealed class Error {
    object CustomerNotFoundError : Error()
    object GenericError : Error()
}
