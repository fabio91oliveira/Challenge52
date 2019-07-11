package oliveira.fabio.challenge52.domain.interactor

import oliveira.fabio.challenge52.model.repository.GoalRepository
import oliveira.fabio.challenge52.model.repository.WeekRepository
import oliveira.fabio.challenge52.persistence.model.entity.Goal
import oliveira.fabio.challenge52.persistence.model.entity.Week
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

class GoalCreateInteractorImpl(
    private val goalRepository: GoalRepository,
    private val weekRepository: WeekRepository
) : GoalCreateInteractor {

    override suspend fun addGoal(goal: Goal) = goalRepository.addGoal(goal)
    override suspend fun addWeeks(goal: Goal, totalWeeks: Int) = weekRepository.addWeeks(createWeeks(goal, totalWeeks))

    private fun createWeeks(goal: Goal, totalWeeks: Int): MutableList<Week> {
        val weeks = mutableListOf<Week>()

        val calendar = Calendar.getInstance()
        calendar.time = goal.initialDate
        var total = goal.valueToStart

        for (i in 1..totalWeeks) {
            Week(i, round(total.toDouble(), 2), calendar.time, false, goal.id).apply {
                weeks.add(this)
            }
            total += goal.valueToStart
            calendar.add(Calendar.DAY_OF_YEAR, 7)
        }

        return weeks
    }

    private fun round(value: Double, div: Int) = BigDecimal(value).setScale(div, RoundingMode.HALF_EVEN).toFloat()
}