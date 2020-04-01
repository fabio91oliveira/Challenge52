package oliveira.fabio.challenge52.persistence.config

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import oliveira.fabio.challenge52.persistence.dao.GoalDao
import oliveira.fabio.challenge52.persistence.dao.GoalWithItemsDao
import oliveira.fabio.challenge52.persistence.dao.ItemDao
import oliveira.fabio.challenge52.persistence.model.converter.BigDecimalTypeConverter
import oliveira.fabio.challenge52.persistence.model.converter.DateConverter
import oliveira.fabio.challenge52.persistence.model.converter.ItemTypeEnumConverter
import oliveira.fabio.challenge52.persistence.model.converter.StatusEnumConverter
import oliveira.fabio.challenge52.persistence.model.entity.GoalEntity
import oliveira.fabio.challenge52.persistence.model.entity.ItemEntity

@Database(entities = [(GoalEntity::class), (ItemEntity::class)], version = 1, exportSchema = false)
@TypeConverters(
    DateConverter::class,
    BigDecimalTypeConverter::class,
    StatusEnumConverter::class,
    ItemTypeEnumConverter::class
)
internal abstract class Database : RoomDatabase() {
    abstract fun goalDao(): GoalDao
    abstract fun goalWithItemsDao(): GoalWithItemsDao
    abstract fun itemsDao(): ItemDao
//    abstract fun weekDao(): WeekDao
}