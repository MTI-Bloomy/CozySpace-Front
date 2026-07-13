package bloomy.cozyspace.auth.signUp

fun isCreatedEmailValid(email: String): Boolean {
    val emailAddressRegex = Regex(
        "^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$"
    )

    return email.isNotBlank() && email.matches(emailAddressRegex);
}

fun isCreatedUsernameValid(username: String): Boolean {
    return username.length > 1 && username.length < 20 && username.isNotBlank() && username.all { it.isLetter() }
    // TODO => change if needed the max length
    // Maybe show errors
}

fun getPasswordStrength(password: String): Int {
    var score = 0

    if (password.length >= 6) score++
    if (password.any { it.isDigit() }) score++
    if (password.any { it.isUpperCase() }) score++
    if (password.any { !it.isLetterOrDigit() }) score++

    return score.coerceIn(0, 4)
}

fun isCreatedPasswordValid(password: String): Boolean {
    return getPasswordStrength(password) >= 4
}

fun isCreatedPasswordConfirmationValid(password: String, passwordConfirmation: String): Boolean {
    return password == passwordConfirmation && passwordConfirmation != ""
}
