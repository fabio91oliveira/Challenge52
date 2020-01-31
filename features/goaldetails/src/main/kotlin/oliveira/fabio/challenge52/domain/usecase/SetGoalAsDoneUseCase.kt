package oliveira.fabio.challenge52.domain.usecase

import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks

interface SetGoalAsDoneUseCase {
    suspend operator fun invoke(goalWithWeeks: GoalWithWeeks)
}