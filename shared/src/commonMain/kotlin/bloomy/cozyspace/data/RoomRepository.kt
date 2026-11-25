package bloomy.cozyspace.data

import bloomy.cozyspace.data.dto.RoomDto
import bloomy.cozyspace.interfaces.ApiResult
import bloomy.cozyspace.network.ApiService

class RoomRepository(
    private val api: ApiService
) {
    suspend fun getRooms(houseId: String): ApiResult<List<RoomDto>> {
        return api.getRooms(houseId)
    }

    suspend fun placeItem(houseId: String, roomId: String, rewardId: String): ApiResult<RoomDto> {
        return api.placeItem(houseId, roomId, rewardId)
    }

    suspend fun removeItem(houseId: String, roomId: String, rewardId: String): ApiResult<RoomDto> {
        return api.removeItem(houseId, roomId, rewardId)
    }
}
