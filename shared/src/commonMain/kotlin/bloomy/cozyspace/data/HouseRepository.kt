package bloomy.cozyspace.data

import bloomy.cozyspace.data.dto.HouseDto
import bloomy.cozyspace.data.dto.createHouseRequestDto
import bloomy.cozyspace.interfaces.ApiResult
import bloomy.cozyspace.network.ApiService

class HouseRepository(
    private val api: ApiService
) {
    suspend fun getHouse(): ApiResult<List<HouseDto>> {
        return api.getHouse();
    }

    suspend fun createHouse(request: createHouseRequestDto): ApiResult<HouseDto> {
        return api.createHouse(request)
    }
}
