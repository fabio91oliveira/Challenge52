package oliveira.fabio.challenge52.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.repository.ItemRepository
import oliveira.fabio.challenge52.domain.usecase.ChangeItemStatusUseCase
import oliveira.fabio.challenge52.presentation.vo.Goal
import oliveira.fabio.challenge52.presentation.vo.ItemDetail

internal class ChangeItemStatusUseCaseImpl(
    private val itemRepository: ItemRepository
) : ChangeItemStatusUseCase {
    override suspend fun invoke(
        itemDetail: ItemDetail,
        goal: Goal
    ) = withContext(Dispatchers.IO) {
        delay(2000L)
        with(itemDetail) {
            isChecked = isChecked.not()
            goal.items.first { it.id == itemDetail.id }.isChecked = isChecked
            itemRepository.updateItemStatus(
                idGoal = idGoal,
                idItem = id,
                isChecked = isChecked
            )
        }
    }
}