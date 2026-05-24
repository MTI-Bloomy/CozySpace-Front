package bloomy.cozyspace.login.signUp

const val ERR_LEN = "Password must have at least eight characters !"
const val ERR_WHITESPACE = "Password must not contain whitespace !"
const val ERR_DIGIT = "Password must contain at least one digit !"
const val ERR_UPPER = "Password must have at least one uppercase letter !"
const val ERR_SPECIAL = "Password must have at least one special character, such as: _%-=+#@"

fun isCreatedEmailValid(email: String): Boolean {
    val emailAddressRegex = Regex(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
    )

    return email.isNotBlank() && email.matches(emailAddressRegex);
}

fun isCreatedUsernameValid(username: String): Boolean {
    return username.isNotBlank() && username.length > 1 && username.length < 20
    // TODO => change if needed the max length
}

fun isCreatedPasswordValid(password: String) = runCatching {
    require(password.length >= 6) { ERR_LEN }
    require(password.none { it.isWhitespace() }) { ERR_WHITESPACE }
    require(password.any { it.isDigit() }) { ERR_DIGIT }
    require(password.any { it.isUpperCase() }) { ERR_UPPER }
    require(password.any { !it.isLetterOrDigit() }) { ERR_SPECIAL }
}

fun isCreatedPasswordConfirmationValid(password: String, passwordConfirmation: String): Boolean {
    return password == passwordConfirmation && passwordConfirmation != ""
}
