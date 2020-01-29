package oliveira.fabio.challenge52.domain.usecase.impl

import oliveira.fabio.challenge52.domain.repository.GoalRepository
import oliveira.fabio.challenge52.domain.usecase.SetGoalAsDoneUseCase
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks

class SetGoalAsDoneUseCaseImpl(
    private val goalRepository: GoalRepository
) : SetGoalAsDoneUseCase {
    override suspend operator fun invoke(goalWithWeeks: GoalWithWeeks) {
        with(goalWithWeeks) {
            //            weekRepository.updateWeeks(weeks)
            goal.apply {
                isDone = true
                goalRepository.updateGoal(this)
            }
        }
    }
}