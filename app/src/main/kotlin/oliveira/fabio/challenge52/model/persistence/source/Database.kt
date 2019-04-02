package oliveira.fabio.challenge52.model.persistence.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import oliveira.fabio.challenge52.model.entity.Goal
import oliveira.fabio.challenge52.model.entity.Week
import oliveira.fabio.challenge52.model.persistence.converter.DateConverter
import oliveira.fabio.challenge52.model.persistence.dao.GoalDao
import oliveira.fabio.challenge52.model.persistence.dao.WeekDao

@Database(entities = [(Goal::class), (Week::class)], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class Database : RoomDatabase() {
    abstract fun goalDao(): GoalDao
    abstract fun weekDao(): WeekDao
}