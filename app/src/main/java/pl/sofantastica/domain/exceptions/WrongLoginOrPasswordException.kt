package pl.sofantastica.domain.exceptions

class WrongLoginOrPasswordException: Exception(SIGN_IN_ERROR) {
    private companion object {
        const val SIGN_IN_ERROR = "Incorrect User Email or Password"
    }
}