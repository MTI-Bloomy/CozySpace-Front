package bloomy.cozyspace.data

import bloomy.cozyspace.data.dto.TimerMusicDto
import bloomy.cozyspace.interfaces.ApiResult
import bloomy.cozyspace.network.ApiService

class TimerMusicRepository(
    private val api: ApiService
) {
    suspend fun getTimersMusics(): ApiResult<List<TimerMusicDto>> {
        return api.getTimersMusics();
    }
}
