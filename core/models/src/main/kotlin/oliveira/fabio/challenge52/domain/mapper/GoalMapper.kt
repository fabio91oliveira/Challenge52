package oliveira.fabio.challenge52.domain.mapper

import oliveira.fabio.challenge52.persistence.model.entity.GoalWithItemsEntity
import oliveira.fabio.challenge52.presentation.vo.Goal

interface GoalMapper {
    operator fun invoke(goalWithItemsEntity: GoalWithItemsEntity): Goal
}