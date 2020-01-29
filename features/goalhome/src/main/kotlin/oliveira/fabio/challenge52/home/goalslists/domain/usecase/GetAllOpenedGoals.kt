package oliveira.fabio.challenge52.home.goalslists.domain.usecase

import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks

interface GetAllOpenedGoals {
    suspend operator fun invoke(): List<GoalWithWeeks>
}