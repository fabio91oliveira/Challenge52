package oliveira.fabio.challenge52.data.mapper

import oliveira.fabio.challenge52.persistence.model.entity.ItemEntity
import oliveira.fabio.challenge52.presentation.vo.GoalToSave

internal interface ItemEntityMapper {
    operator fun invoke(goalToSave: GoalToSave, idGoal: Long): List<ItemEntity>
}