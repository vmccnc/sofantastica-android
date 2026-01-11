package pl.sofantastica.domain.exceptions

class SignUpException: Exception(SIGN_UP_ERROR) {
    private companion object {
        const val SIGN_UP_ERROR = "Can't create new User. Check your Email and Internet Access"
    }
}