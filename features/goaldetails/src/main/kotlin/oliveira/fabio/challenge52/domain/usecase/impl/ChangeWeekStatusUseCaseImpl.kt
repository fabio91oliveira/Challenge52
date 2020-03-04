package oliveira.fabio.challenge52.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.model.Week
import oliveira.fabio.challenge52.domain.repository.WeekRepository
import oliveira.fabio.challenge52.domain.usecase.ChangeWeekStatusUseCase

internal class ChangeWeekStatusUseCaseImpl(
    private val weekRepository: WeekRepository
) : ChangeWeekStatusUseCase {
    override suspend fun invoke(week: Week) = withContext(Dispatchers.IO) {
        with(week) {
            isChecked = isChecked.not()
            weekRepository.updateWeekStatus(
                goalId = idGoal,
                weekId = id,
                isChecked = isChecked
            )
        }
    }
}