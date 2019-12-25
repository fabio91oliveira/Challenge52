package oliveira.fabio.challenge52.domain.usecase.impl

import oliveira.fabio.challenge52.domain.repository.GoalRepository
import oliveira.fabio.challenge52.domain.repository.WeekRepository
import oliveira.fabio.challenge52.domain.usecase.RemoveGoalUseCase
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks

class RemoveGoalUseCaseImpl(
    private val goalRepository: GoalRepository,
    private val weekRepository: WeekRepository
) : RemoveGoalUseCase {
    override suspend operator fun invoke(goalWithWeeks: GoalWithWeeks) {
        with(goalWithWeeks) {
            goalRepository.removeGoal(goal)
            weekRepository.removeWeeks(weeks)
        }
    }
}