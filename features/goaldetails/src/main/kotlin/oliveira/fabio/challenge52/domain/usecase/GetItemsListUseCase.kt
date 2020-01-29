package oliveira.fabio.challenge52.domain.usecase

import oliveira.fabio.challenge52.domain.model.vo.Item
import oliveira.fabio.challenge52.persistence.model.entity.Week
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks

interface GetItemsListUseCase {
    suspend operator fun invoke(goalWithWeeks: GoalWithWeeks, week: Week? = null): MutableList<Item>
}