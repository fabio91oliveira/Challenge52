package oliveira.fabio.challenge52.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.usecase.CheckAllWeeksAreDepositedUseCase
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks

class CheckAllWeeksAreDepositedUseCaseImpl : CheckAllWeeksAreDepositedUseCase {
    override suspend fun invoke(goalWithWeeks: GoalWithWeeks) = withContext(Dispatchers.Default) {
        goalWithWeeks.weeks.forEach {
            if (!it.isDeposited) return@withContext false
        }
        return@withContext true
    }
}