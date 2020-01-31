package oliveira.fabio.challenge52.domain.mapper

import oliveira.fabio.challenge52.domain.model.vo.Item
import oliveira.fabio.challenge52.persistence.model.entity.Week
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks

interface ItemMapper {
    operator fun invoke(goalWithWeeks: GoalWithWeeks, week: Week?): MutableList<Item>
}