package oliveira.fabio.challenge52.model.persistence.dao

import androidx.room.*
import oliveira.fabio.challenge52.model.entity.Target

@Dao
interface TargetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTarget(target: Target): Long

    @Update
    fun updateTarget(target: Target)

    @Delete
    fun deleteTarget(target: Target): Int

    @Query("SELECT * FROM target")
    fun getAllTargets(): List<Target>
}