package oliveira.fabio.challenge52.domain.usecase.impl

import oliveira.fabio.challenge52.domain.repository.GoalRepository
import oliveira.fabio.challenge52.domain.repository.WeekRepository
import oliveira.fabio.challenge52.domain.usecase.GoalDetailsUseCase
import oliveira.fabio.challenge52.persistence.model.entity.Goal
import oliveira.fabio.challenge52.persistence.model.entity.Week
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks

class GoalDetailsUseCaseImpl(
    private val goalRepository: GoalRepository,
    private val weekRepository: WeekRepository
) : GoalDetailsUseCase {

    override fun changeWeekDepositStatus(week: Week) {
        week.isDeposited = !week.isDeposited
    }

    override fun setGoalAsDone(goalWithWeeks: GoalWithWeeks) {
        goalWithWeeks.goal.isDone = true
    }

    override suspend fun updateWeek(week: Week) = weekRepository.updateWeek(week)
    override suspend fun removeGoal(goal: Goal) = goalRepository.removeGoal(goal)
    override suspend fun removeWeeks(weeks: List<Week>) = weekRepository.removeWeeks(weeks)
    override suspend fun updateWeeks(weeks: List<Week>) = weekRepository.updateWeeks(weeks)
    override suspend fun updateGoal(goal: Goal) = goalRepository.updateGoal(goal)

}