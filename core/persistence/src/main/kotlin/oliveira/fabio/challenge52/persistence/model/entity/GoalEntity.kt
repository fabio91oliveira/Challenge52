package oliveira.fabio.challenge52.persistence.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import oliveira.fabio.challenge52.persistence.model.enums.GoalStatusEnum
import oliveira.fabio.challenge52.persistence.model.enums.ItemTypeEnum
import java.io.Serializable
import java.math.BigDecimal

@Entity(tableName = "goal")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var idChallenge: Long,
    var goalStatus: GoalStatusEnum,
    var type: ItemTypeEnum,
    var name: String,
    var startMoneyToSave: BigDecimal,
    var totalWeeks: Int
) : Serializable