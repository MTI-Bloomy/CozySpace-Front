package bloomy.cozyspace

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform