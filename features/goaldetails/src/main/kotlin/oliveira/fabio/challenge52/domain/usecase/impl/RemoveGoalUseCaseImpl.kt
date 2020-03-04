package oliveira.fabio.challenge52.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.model.Goal
import oliveira.fabio.challenge52.domain.repository.GoalRepository
import oliveira.fabio.challenge52.domain.repository.WeekRepository
import oliveira.fabio.challenge52.domain.usecase.RemoveGoalUseCase

internal class RemoveGoalUseCaseImpl(
    private val goalRepository: GoalRepository,
    private val weekRepository: WeekRepository
) : RemoveGoalUseCase {
    override suspend operator fun invoke(goal: Goal) {
        withContext(Dispatchers.IO) {
            goalRepository.removeGoal(goal.id)
            weekRepository.removeWeeksByIdGoal(goal.id)
        }
    }
}