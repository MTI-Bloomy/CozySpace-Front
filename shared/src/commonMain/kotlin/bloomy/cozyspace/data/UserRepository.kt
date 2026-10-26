package bloomy.cozyspace.data

import bloomy.cozyspace.data.dto.LoginDto
import bloomy.cozyspace.data.dto.LoginRequestDto
import bloomy.cozyspace.data.dto.RegisterDto
import bloomy.cozyspace.data.dto.RegisterRequestDto
import bloomy.cozyspace.data.dto.UserDto
import bloomy.cozyspace.interfaces.ApiResult
import bloomy.cozyspace.network.ApiService

class UserRepository(
    private val api: ApiService
) {

    suspend fun register(request: RegisterRequestDto): ApiResult<RegisterDto> {
        return api.signup(request)
    }

    suspend fun login(request: LoginRequestDto): ApiResult<LoginDto> {
        return api.signin(request)
    }

    suspend fun getUser(): ApiResult<UserDto> {
        return api.getUser()
    }
}
