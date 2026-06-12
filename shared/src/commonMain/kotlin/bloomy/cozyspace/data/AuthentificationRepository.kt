package bloomy.cozyspace.data

import bloomy.cozyspace.network.ApiService

class AuthentificationRepository(
    private val api: ApiService
) {

    suspend fun register(request: RegisterRequestDto): RegisterDto {
        return api.signup(request)
    }

    suspend fun login(request: LoginRequestDto): LoginDto {
        return api.signin(request)
    }
}
