package oliveira.fabio.challenge52.persistence.model.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "goal")
data class GoalEntity(
    var name: String = "",
    var valueToStart: Float = 0f,
    var isDone: Boolean = false,
    @Ignore
    var initialDate: Date = Date(),
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
) :
    Serializable