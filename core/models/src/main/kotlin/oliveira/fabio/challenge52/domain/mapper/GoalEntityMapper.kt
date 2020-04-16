package oliveira.fabio.challenge52.domain.mapper

import oliveira.fabio.challenge52.persistence.model.entity.GoalEntity
import oliveira.fabio.challenge52.presentation.vo.GoalToSave

interface GoalEntityMapper {
    operator fun invoke(goalToSave: GoalToSave): GoalEntity
}