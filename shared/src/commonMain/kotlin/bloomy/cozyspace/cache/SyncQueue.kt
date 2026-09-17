package bloomy.cozyspace.cache

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SyncQueue(
    private val storage: SyncQueueStorage,
    private val scope: CoroutineScope,
) {
    private val _pending = MutableStateFlow<List<PendingAction>>(emptyList())
    val pending: StateFlow<List<PendingAction>> = _pending.asStateFlow()

    suspend fun load() {
        _pending.value = storage.get()
    }

    fun enqueue(action: PendingAction) {
        _pending.update { it + action }
        persist()
    }

    fun remove(actionId: String) {
        _pending.update { list -> list.filterNot { it.id == actionId } }
        persist()
    }

    fun clear() {
        _pending.update { emptyList() }
        persist()
    }

    private fun persist() {
        scope.launch { storage.save(_pending.value) }
    }
}
