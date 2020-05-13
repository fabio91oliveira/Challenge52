package oliveira.fabio.challenge52.persistence.model.entity

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable

class GoalWithItemsEntity : Serializable {
    @Embedded
    lateinit var goal: GoalEntity

    @Relation(
        parentColumn = "id",
        entityColumn = "idGoal",
        entity = ItemEntity::class
    )
    lateinit var items: List<ItemEntity>
}