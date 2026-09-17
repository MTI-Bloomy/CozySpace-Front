package bloomy.cozyspace.utils

fun randomId(): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    return (1..20).map { chars.random() }.joinToString("")
}
