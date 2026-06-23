package bloomy.cozyspace.data

import bloomy.cozyspace.interfaces.ApiResult
import bloomy.cozyspace.network.ApiService

class AuthentificationRepository(
    private val api: ApiService
) {

    suspend fun register(
        request: RegisterRequestDto
    ): ApiResult<RegisterDto> {
        return api.signup(request)
    }

    suspend fun login(
        request: LoginRequestDto
    ): ApiResult<LoginDto> {
        return api.signin(request)
    }
}
