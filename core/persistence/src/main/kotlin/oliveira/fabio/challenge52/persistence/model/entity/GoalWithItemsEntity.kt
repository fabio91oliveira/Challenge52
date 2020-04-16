package oliveira.fabio.challenge52.persistence.model.entity

import androidx.room.Embedded
import androidx.room.Relation
import oliveira.fabio.challenge52.persistence.model.entity.GoalEntity
import oliveira.fabio.challenge52.persistence.model.entity.ItemEntity
import java.io.Serializable

class GoalWithItemsEntity : Serializable {
    @Embedded
    lateinit var goal: GoalEntity

    @Relation(
        parentColumn = "id",
        entityColumn = "idGoal",
        entity = ItemEntity::class
    )
    lateinit var items: List<ItemEntity>
//    @Ignore
//    var lastPosition: Int? = null
//
//    fun getRemainingWeeksCount(): Int {
//        var remainingWeeksCount = 0
//
//        weeks.forEach {
//            if (!it.isDeposited) remainingWeeksCount++
//        }
//
//        return remainingWeeksCount
//    }
//
//    fun getWeeksDepositedCount(): Int {
//        var weeksCount = 0
//
//        weeks.forEach {
//            if (it.isDeposited) weeksCount++
//        }
//
//        return weeksCount
//    }
//
//    fun getPercentOfConclusion() =
//        (((weeks.size.toFloat() - getRemainingWeeksCount().toFloat()) / weeks.size.toFloat()) * PERCENT).toInt()
//
//    fun getTotal(): Float {
//        var total = 0f
//        weeks.forEach {
//            total += it.spittedValue
//        }
//
//        return total
//    }
//
//    fun getTotalAccumulated(): Float {
//        var total = 0f
//        weeks.forEach {
//            if (it.isDeposited) total += it.spittedValue
//        }
//
//        return total
//    }
//
//    fun getStartDate() = weeks[FIRST_INDEX].date
//    fun getEndDate() = weeks[LAST_INDEX].date

    companion object {
        private const val FIRST_INDEX = 0
        private const val LAST_INDEX = 51
        private const val PERCENT = 100
    }
}