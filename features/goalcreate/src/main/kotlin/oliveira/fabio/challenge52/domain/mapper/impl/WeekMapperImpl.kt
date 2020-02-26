package oliveira.fabio.challenge52.domain.mapper.impl

import oliveira.fabio.challenge52.domain.mapper.WeekMapper
import oliveira.fabio.challenge52.persistence.model.entity.Goal
import oliveira.fabio.challenge52.persistence.model.entity.Week
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

class WeekMapperImpl : WeekMapper {
    override fun invoke(goal: Goal, id: Long) = mutableListOf<Week>().apply {
        goal.id = id

        Calendar.getInstance().apply {
            time = goal.initialDate
            var total = goal.valueToStart

            for (i in FIRST_ITEM..TOTAL_WEEKS) {
                Week(
                    i, round(
                        total.toDouble(),
                        DIV_ROUND
                    ), time, false, goal.id
                ).also { week ->
                    add(week)
                }
                total += goal.valueToStart
                add(
                    Calendar.DAY_OF_YEAR,
                    DAY_OF_YEAR
                )
            }
        }
    }

    private fun round(value: Double, div: Int) =
        BigDecimal(value).setScale(div, RoundingMode.HALF_EVEN).toFloat()

    companion object {
        private const val TOTAL_WEEKS = 52
        private const val FIRST_ITEM = 1
        private const val DIV_ROUND = 2
        private const val DAY_OF_YEAR = 7
    }
}