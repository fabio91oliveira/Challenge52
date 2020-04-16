package oliveira.fabio.challenge52.domain.mapper

import oliveira.fabio.challenge52.persistence.model.entity.ItemEntity
import oliveira.fabio.challenge52.presentation.vo.GoalToSave

interface ItemEntityMapper {
    operator fun invoke(goalToSave: GoalToSave, idGoal: Long): List<ItemEntity>
}