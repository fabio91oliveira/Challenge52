package oliveira.fabio.challenge52.domain.mapper

import oliveira.fabio.challenge52.persistence.model.entity.Goal
import oliveira.fabio.challenge52.persistence.model.entity.Week

interface WeekMapper {
    operator fun invoke(goal: Goal, id: Long): MutableList<Week>
}