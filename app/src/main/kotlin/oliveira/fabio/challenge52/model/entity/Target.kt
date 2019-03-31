package oliveira.fabio.challenge52.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable
import java.util.*

@Entity(tableName = "target")
data class Target(
    var name: String, var isCompleted: Boolean, var totalValue: Double, var initialDate: Date
) :
    Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}