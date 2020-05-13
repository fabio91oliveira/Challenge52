package oliveira.fabio.challenge52.data.mapper

import oliveira.fabio.challenge52.persistence.model.entity.GoalEntity
import oliveira.fabio.challenge52.presentation.vo.GoalToSave

internal interface GoalEntityMapper {
    operator fun invoke(goalToSave: GoalToSave): GoalEntity
}