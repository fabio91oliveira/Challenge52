package oliveira.fabio.challenge52.goal.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.goal.domain.usecase.AddGoalUseCase
import oliveira.fabio.challenge52.domain.repository.GoalRepository
import oliveira.fabio.challenge52.presentation.vo.GoalToSave

internal class AddGoalUseCaseImpl(
    private val goalRepository: GoalRepository
) : AddGoalUseCase {
    override suspend fun invoke(goalToSave: GoalToSave) = withContext(Dispatchers.IO) {
        goalRepository.addGoal(goalToSave)
    }
}