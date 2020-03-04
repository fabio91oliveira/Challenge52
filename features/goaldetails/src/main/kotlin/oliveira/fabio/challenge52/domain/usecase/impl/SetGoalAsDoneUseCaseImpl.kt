package oliveira.fabio.challenge52.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.model.Goal
import oliveira.fabio.challenge52.domain.repository.GoalRepository
import oliveira.fabio.challenge52.domain.usecase.SetGoalAsDoneUseCase

internal class SetGoalAsDoneUseCaseImpl(
    private val goalRepository: GoalRepository
) : SetGoalAsDoneUseCase {
    override suspend operator fun invoke(goal: Goal) {
        withContext(Dispatchers.IO) {
            goalRepository.setGoalAsDone(goal.id)
        }
    }
}