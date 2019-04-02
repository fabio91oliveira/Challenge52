package oliveira.fabio.challenge52.model.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import oliveira.fabio.challenge52.model.entity.Week

@Dao
interface WeekDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addWeeks(weeks: List<Week>): List<Long>

    @Update
    fun updateWeek(week: Week)
}