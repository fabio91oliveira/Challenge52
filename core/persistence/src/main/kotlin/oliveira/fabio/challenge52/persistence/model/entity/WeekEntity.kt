package oliveira.fabio.challenge52.persistence.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "week")
data class WeekEntity(
    var position: Int,
    var spittedValue: Float,
    var date: Date,
    var isDeposited: Boolean,
    var idGoal: Long
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}