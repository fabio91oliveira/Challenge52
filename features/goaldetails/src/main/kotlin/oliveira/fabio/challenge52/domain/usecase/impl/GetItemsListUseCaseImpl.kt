package oliveira.fabio.challenge52.domain.usecase.impl

import oliveira.fabio.challenge52.domain.mapper.ItemMapper
import oliveira.fabio.challenge52.domain.usecase.GetItemsListUseCase
import oliveira.fabio.challenge52.persistence.model.entity.Week
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks

class GetItemsListUseCaseImpl(
    private val itemMapper: ItemMapper
) : GetItemsListUseCase {
    override fun getItemList(goalWithWeeks: GoalWithWeeks, week: Week?) =
        itemMapper(goalWithWeeks, week)
}