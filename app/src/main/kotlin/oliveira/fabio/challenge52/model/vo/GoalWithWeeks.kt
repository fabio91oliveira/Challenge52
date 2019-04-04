package oliveira.fabio.challenge52.model.vo

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import oliveira.fabio.challenge52.model.entity.Goal
import oliveira.fabio.challenge52.model.entity.Week
import java.io.Serializable

class GoalWithWeeks : Serializable {
    @Embedded
    lateinit var goal: Goal
    @Relation(
        parentColumn = "id",
        entityColumn = "idGoal",
        entity = Week::class
    )
    lateinit var weeks: List<Week>
    @Ignore
    var isSelected = false

    fun getRemainingWeeksCount(): Int {
        var remainingWeeksCount = 0

        weeks.forEach {
            if (!it.isDeposited) remainingWeeksCount++
        }

        return remainingWeeksCount
    }

    fun getPercentOfConclusion() = (((weeks.size.toFloat() - getRemainingWeeksCount().toFloat()) / weeks.size.toFloat()) * PERCENT).toInt()

    fun getStartDate() = weeks[FIRST_INDEX].date
    fun getEndDate() = weeks[LAST_INDEX].date

    companion object {
        private const val FIRST_INDEX = 0
        private const val LAST_INDEX = 51
        private const val PERCENT = 100
    }
}