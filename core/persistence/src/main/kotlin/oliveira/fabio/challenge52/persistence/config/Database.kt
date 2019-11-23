package oliveira.fabio.challenge52.persistence.config

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import oliveira.fabio.challenge52.persistence.dao.GoalDao
import oliveira.fabio.challenge52.persistence.dao.GoalWithWeeksDao
import oliveira.fabio.challenge52.persistence.dao.WeekDao
import oliveira.fabio.challenge52.persistence.model.entity.Goal
import oliveira.fabio.challenge52.persistence.model.entity.Week
import oliveira.fabio.challenge52.persistence.model.entity.converter.DateConverter

@Database(entities = [(Goal::class), (Week::class)], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class Database : RoomDatabase() {
    abstract fun goalDao(): GoalDao
    abstract fun goalWithWeeksDao(): GoalWithWeeksDao
    abstract fun weekDao(): WeekDao
}