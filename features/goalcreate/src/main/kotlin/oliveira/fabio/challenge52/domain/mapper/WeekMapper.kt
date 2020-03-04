package oliveira.fabio.challenge52.domain.mapper

import oliveira.fabio.challenge52.persistence.model.entity.GoalEntity
import oliveira.fabio.challenge52.persistence.model.entity.WeekEntity

interface WeekMapper {
    operator fun invoke(goal: GoalEntity, id: Long): MutableList<WeekEntity>
}