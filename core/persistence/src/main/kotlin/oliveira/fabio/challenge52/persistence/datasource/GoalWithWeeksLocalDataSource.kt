package oliveira.fabio.challenge52.persistence.datasource

import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks

interface GoalWithWeeksLocalDataSource {
    fun getAllOpenedGoalsWithWeeks(): List<GoalWithWeeks>
    fun getAllDoneGoalsWithWeeks(): List<GoalWithWeeks>
}