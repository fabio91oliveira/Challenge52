package oliveira.fabio.challenge52.persistence.datasource

import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks

interface GoalWithWeeksLocalDataSource {
    fun getAllGoalsWithWeeks(): List<GoalWithWeeks>
    fun getDoneAllGoalsWithWeeks(): List<GoalWithWeeks>
}