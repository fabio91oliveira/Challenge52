package oliveira.fabio.challenge52.persistence.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import oliveira.fabio.challenge52.persistence.model.enums.GoalStatusEnum
import oliveira.fabio.challenge52.persistence.model.enums.PeriodTypeEnum
import java.io.Serializable
import java.util.*

@Entity(tableName = "goal")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var idChallenge: Long,
    var goalStatus: GoalStatusEnum,
    var name: String,
    val currentLocale: Locale,
    var periodType: PeriodTypeEnum,
    var totalPeriod: Int,
    var totalMoney: Double
) : Serializable