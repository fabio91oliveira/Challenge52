package oliveira.fabio.challenge52.domain.interactor

import oliveira.fabio.challenge52.domain.model.vo.Item
import oliveira.fabio.challenge52.persistence.model.entity.Goal
import oliveira.fabio.challenge52.persistence.model.entity.Week
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks

interface GoalDetailsInteractor {
    fun parseToDetailsList(goalWithWeeks: GoalWithWeeks, week: Week? = null): MutableList<Item>
    suspend fun updateWeek(week: Week)
    suspend fun removeGoal(goal: Goal): Int
    suspend fun removeWeeks(weeks: List<Week>): Int
    suspend fun updateWeeks(weeks: List<Week>)
    suspend fun updateGoal(goal: Goal)
}