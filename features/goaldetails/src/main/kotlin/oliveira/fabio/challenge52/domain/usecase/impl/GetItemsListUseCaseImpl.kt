package oliveira.fabio.challenge52.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.mapper.ItemMapper
import oliveira.fabio.challenge52.domain.usecase.GetItemsListUseCase
import oliveira.fabio.challenge52.persistence.model.entity.Week
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks

class GetItemsListUseCaseImpl(
    private val itemMapper: ItemMapper
) : GetItemsListUseCase {
    override suspend operator fun invoke(goalWithWeeks: GoalWithWeeks, week: Week?) =
        withContext(Dispatchers.Main) {
            itemMapper(goalWithWeeks, week)
        }

}