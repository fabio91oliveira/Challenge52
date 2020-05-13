package oliveira.fabio.challenge52.data.mapper

import oliveira.fabio.challenge52.persistence.model.entity.GoalWithItemsEntity
import oliveira.fabio.challenge52.presentation.vo.Goal

internal interface GoalMapper {
    operator fun invoke(goalWithItemsEntity: GoalWithItemsEntity): Goal
}