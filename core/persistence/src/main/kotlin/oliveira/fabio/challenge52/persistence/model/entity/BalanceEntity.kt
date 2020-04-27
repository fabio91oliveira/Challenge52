package oliveira.fabio.challenge52.persistence.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "balance")
data class BalanceEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val currentLocale: Locale,
    val startDate: Date
) : Serializable