package oliveira.fabio.challenge52.persistence.datasource

import oliveira.fabio.challenge52.persistence.model.vo.GoalWithItemsEntity

interface GoalWithWeeksLocalDataSource {
    fun getAllOpenedGoalsWithWeeks(): List<GoalWithItemsEntity>
    fun getAllDoneGoalsWithWeeks(): List<GoalWithItemsEntity>
}