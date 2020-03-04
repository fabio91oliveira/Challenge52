package oliveira.fabio.challenge52.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.model.Week
import oliveira.fabio.challenge52.domain.usecase.VerifyAllWeekAreCompletedUseCase

internal class VerifyAllWeekAreCompletedUseCaseImpl : VerifyAllWeekAreCompletedUseCase {
    override suspend fun invoke(weeks: List<Week>) = withContext(Dispatchers.Default) {
        weeks.forEach {
            if (it.isChecked.not()) return@withContext false
        }
        return@withContext true
    }
}