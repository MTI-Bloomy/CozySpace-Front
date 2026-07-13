package bloomy.cozyspace.data

import bloomy.cozyspace.data.dto.ChooseRewardRequestDto
import bloomy.cozyspace.data.dto.RewardDto
import bloomy.cozyspace.interfaces.ApiResult
import bloomy.cozyspace.network.ApiService

class RewardRepository(
    private val api: ApiService
) {
    suspend fun getRewards(): ApiResult<List<RewardDto>> {
        return api.getRewards()
    }

    suspend fun getReward(rewardId: String): ApiResult<RewardDto> {
        return api.getReward(rewardId)
    }

    suspend fun chooseReward(request: ChooseRewardRequestDto): ApiResult<RewardDto> {
        return api.chooseReward(request)
    }
}
