package oliveira.fabio.challenge52.persistence.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "item")
data class ItemEntity(
    var idGoal: Long,
    var position: Int,
    var value: Float,
    var date: Date,
    var isSaved: Boolean
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}