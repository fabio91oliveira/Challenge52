package oliveira.fabio.challenge52.home.goalslists.domain.usecase

import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks

interface GetAllDoneGoals {
    suspend operator fun invoke(): List<GoalWithWeeks>
}