package pl.sofantastica.domain.exceptions

class IsNotOnlineException: Exception(ERROR_MESSAGE) {
    companion object {
        const val ERROR_MESSAGE = "Can't load data. You're not online"
    }
}