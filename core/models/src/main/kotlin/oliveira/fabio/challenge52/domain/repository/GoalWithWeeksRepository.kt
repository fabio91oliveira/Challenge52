package oliveira.fabio.challenge52.domain.repository

import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks

interface GoalWithWeeksRepository {
    fun getAllOpenedGoalsWithWeeks(): List<GoalWithWeeks>
    fun getAllDoneGoalsWithWeeks(): List<GoalWithWeeks>
}