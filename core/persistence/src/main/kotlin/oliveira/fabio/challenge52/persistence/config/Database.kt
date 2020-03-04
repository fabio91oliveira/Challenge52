package oliveira.fabio.challenge52.persistence.config

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import oliveira.fabio.challenge52.persistence.dao.GoalDao
import oliveira.fabio.challenge52.persistence.dao.GoalWithWeeksDao
import oliveira.fabio.challenge52.persistence.dao.WeekDao
import oliveira.fabio.challenge52.persistence.model.entity.GoalEntity
import oliveira.fabio.challenge52.persistence.model.entity.WeekEntity
import oliveira.fabio.challenge52.persistence.model.entity.converter.DateConverter

@Database(entities = [(GoalEntity::class), (WeekEntity::class)], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
internal abstract class Database : RoomDatabase() {
    abstract fun goalDao(): GoalDao
    abstract fun goalWithWeeksDao(): GoalWithWeeksDao
    abstract fun weekDao(): WeekDao
}