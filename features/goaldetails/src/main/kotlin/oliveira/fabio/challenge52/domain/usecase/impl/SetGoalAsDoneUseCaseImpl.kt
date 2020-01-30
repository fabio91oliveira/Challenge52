package oliveira.fabio.challenge52.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.repository.GoalRepository
import oliveira.fabio.challenge52.domain.usecase.SetGoalAsDoneUseCase
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks

class SetGoalAsDoneUseCaseImpl(
    private val goalRepository: GoalRepository
) : SetGoalAsDoneUseCase {
    override suspend operator fun invoke(goalWithWeeks: GoalWithWeeks) {
        withContext(Dispatchers.IO) {
            with(goalWithWeeks) {
                goal.apply {
                    isDone = true
                    goalRepository.updateGoal(this)
                }
            }
        }
    }
}