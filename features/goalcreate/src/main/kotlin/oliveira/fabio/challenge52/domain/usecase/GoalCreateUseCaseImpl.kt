package oliveira.fabio.challenge52.domain.usecase

import oliveira.fabio.challenge52.model.repository.GoalRepository
import oliveira.fabio.challenge52.model.repository.WeekRepository
import oliveira.fabio.challenge52.persistence.model.entity.Goal
import oliveira.fabio.challenge52.persistence.model.entity.Week
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

class GoalCreateUseCaseImpl(
    private val goalRepository: GoalRepository,
    private val weekRepository: WeekRepository
) : GoalCreateUseCase {

    override suspend fun addGoal(goal: Goal) = goalRepository.addGoal(goal)
    override suspend fun addWeeks(goal: Goal, id: Long) = weekRepository.addWeeks(createWeeks(goal, id))

//    private fun createWeeks(goal: Goal, id: Long): MutableList<Week> {
//
//        val weeks = mutableListOf<Week>()
//
//        val calendar = Calendar.getInstance()
//        calendar.time = goal.initialDate
//        var total = goal.valueToStart
//
//        for (i in 1..TOTAL_WEEKS) {
//            Week(i, round(total.toDouble(), 2), calendar.time, false, goal.id).apply {
//                weeks.add(this)
//            }
//            total += goal.valueToStart
//            calendar.add(Calendar.DAY_OF_YEAR, 7)
//        }
//
//        return weeks
//    }

    private fun createWeeks(goal: Goal, id: Long) = mutableListOf<Week>().apply {
        goal.id = id

        Calendar.getInstance().also { cal ->
            cal.time = goal.initialDate
            var total = goal.valueToStart

            for (i in 1..TOTAL_WEEKS) {
                Week(i, round(total.toDouble(), 2), cal.time, false, goal.id).also { week ->
                    add(week)
                }
                total += goal.valueToStart
                cal.add(Calendar.DAY_OF_YEAR, 7)
            }
        }
    }

    private fun round(value: Double, div: Int) = BigDecimal(value).setScale(div, RoundingMode.HALF_EVEN).toFloat()

    companion object {
        private const val TOTAL_WEEKS = 52
    }
}