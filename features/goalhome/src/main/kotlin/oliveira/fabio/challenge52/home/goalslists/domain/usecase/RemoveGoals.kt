package oliveira.fabio.challenge52.home.goalslists.domain.usecase

import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks

interface RemoveGoals {
    suspend operator fun invoke(goalWithWeeksList: MutableList<GoalWithWeeks>)
}