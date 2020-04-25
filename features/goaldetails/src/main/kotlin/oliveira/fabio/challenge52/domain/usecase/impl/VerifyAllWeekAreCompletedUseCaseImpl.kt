package oliveira.fabio.challenge52.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.usecase.VerifyAllWeekAreCompletedUseCase
import oliveira.fabio.challenge52.presentation.vo.Item

internal class VerifyAllWeekAreCompletedUseCaseImpl : VerifyAllWeekAreCompletedUseCase {
    override suspend fun invoke(items: List<Item>) = withContext(Dispatchers.Default) {
        items.forEach {
            if (it.isChecked.not()) return@withContext false
        }
        return@withContext true
    }
}