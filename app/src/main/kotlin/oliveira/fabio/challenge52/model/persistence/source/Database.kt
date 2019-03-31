package oliveira.fabio.challenge52.model.persistence.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import oliveira.fabio.challenge52.model.entity.Target
import oliveira.fabio.challenge52.model.entity.Week
import oliveira.fabio.challenge52.model.persistence.converter.DateConverter
import oliveira.fabio.challenge52.model.persistence.dao.TargetDao

@Database(entities = [(Target::class), (Week::class)], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class Database : RoomDatabase() {
    abstract fun targetDao(): TargetDao
}