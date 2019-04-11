package oliveira.fabio.challenge52.persistence.dao

import androidx.room.*
import oliveira.fabio.challenge52.persistence.model.entity.Week

@Dao
interface WeekDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addWeeks(weeks: List<Week>): List<Long>

    @Delete
    fun deleteWeeks(weeksList: List<Week>): Int

    @Update
    fun updateWeek(week: Week)
}