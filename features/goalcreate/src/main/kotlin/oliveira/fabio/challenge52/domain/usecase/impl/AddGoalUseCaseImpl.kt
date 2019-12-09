package oliveira.fabio.challenge52.domain.usecase.impl

import oliveira.fabio.challenge52.domain.repository.GoalRepository
import oliveira.fabio.challenge52.domain.usecase.AddGoalUseCase
import oliveira.fabio.challenge52.persistence.model.entity.Goal

class AddGoalUseCaseImpl(
    private val goalRepository: GoalRepository
) : AddGoalUseCase {
    override suspend operator fun invoke(goal: Goal) = goalRepository.addGoal(goal)
}