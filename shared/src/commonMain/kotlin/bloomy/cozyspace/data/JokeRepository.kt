package bloomy.cozyspace.data

import bloomy.cozyspace.network.ApiService

class JokeRepository(
    private val api: ApiService
) {

    suspend fun getRandomJoke(): JokeDto {
        return api.getRandomJoke()
    }

    suspend fun getRandomDevJoke(): JokeDto {
        return api.getRandomJokeByType("dev")
    }
}
