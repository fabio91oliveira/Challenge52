package oliveira.fabio.challenge52.model.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "goal")
class Goal :
    Serializable {
    lateinit var name: String
    var totalValue: Float = 0f
    var isDone = false
    @Ignore
    lateinit var initialDate: Date
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}