package oliveira.fabio.challenge52.persistence.datasource

import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeksEntity

interface GoalWithWeeksLocalDataSource {
    fun getAllOpenedGoalsWithWeeks(): List<GoalWithWeeksEntity>
    fun getAllDoneGoalsWithWeeks(): List<GoalWithWeeksEntity>
}