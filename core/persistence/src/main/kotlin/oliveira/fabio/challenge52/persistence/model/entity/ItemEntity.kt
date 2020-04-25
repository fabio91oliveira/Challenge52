package oliveira.fabio.challenge52.persistence.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "item")
data class ItemEntity(
    var idGoal: Long,
    var position: Int,
    var date: Date,
    var value: Double,
    var isSaved: Boolean = false
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
