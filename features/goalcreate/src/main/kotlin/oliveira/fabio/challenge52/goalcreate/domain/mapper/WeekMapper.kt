package oliveira.fabio.challenge52.goalcreate.domain.mapper

import oliveira.fabio.challenge52.persistence.model.entity.GoalEntity
import oliveira.fabio.challenge52.persistence.model.entity.ItemEntity

interface WeekMapper {
    operator fun invoke(goal: GoalEntity, id: Long): MutableList<ItemEntity>
}