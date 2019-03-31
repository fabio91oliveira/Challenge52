package oliveira.fabio.challenge52.model.entity

import androidx.room.*
import java.util.*

@Entity(
    tableName = "week",
    foreignKeys = [ForeignKey(
        entity = Target::class,
        parentColumns = ["id"],
        childColumns = ["id_target"],
        onDelete = ForeignKey.NO_ACTION
    )], indices = [Index(value = ["id_target"], name = "target_index")]
)
data class Week(
    var number: Int,
    var isDeposited: Boolean,
    var spittedValue: Double,
    var date: Date,
    @ColumnInfo(name = "id_target")
    var idTarget: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}