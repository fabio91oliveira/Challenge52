package oliveira.fabio.challenge52.domain.repository

import oliveira.fabio.challenge52.presentation.vo.Goal

interface GoalWithWeeksRepository {
    fun getOpenedGoalsList(): List<Goal>
    fun getDoneGoalsList(): List<Goal>
}