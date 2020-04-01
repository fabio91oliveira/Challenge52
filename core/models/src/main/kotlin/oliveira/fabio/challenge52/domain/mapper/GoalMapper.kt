package oliveira.fabio.challenge52.domain.mapper

import oliveira.fabio.challenge52.domain.model.Goal
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithItemsEntity

interface GoalMapper {
    operator fun invoke(goalWithItemsList: GoalWithItemsEntity): Goal
}