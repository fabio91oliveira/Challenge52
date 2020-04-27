package oliveira.fabio.challenge52.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.repository.GoalRepository
import oliveira.fabio.challenge52.domain.repository.ItemRepository
import oliveira.fabio.challenge52.domain.usecase.ChangeItemStatusUseCase
import oliveira.fabio.challenge52.presentation.vo.Goal
import oliveira.fabio.challenge52.presentation.vo.ItemDetail
import oliveira.fabio.challenge52.presentation.vo.enums.StatusEnum

internal class ChangeItemStatusUseCaseImpl(
    private val goalRepository: GoalRepository,
    private val itemRepository: ItemRepository
) : ChangeItemStatusUseCase {
    override suspend fun invoke(
        itemDetail: ItemDetail,
        goal: Goal
    ) = withContext(Dispatchers.IO) {
        delay(2000L)
        with(itemDetail) {
            isChecked = isChecked.not()

            getSelectedItem(goal, itemDetail).isChecked = isChecked
            itemRepository.updateItemStatus(
                idGoal = idGoal,
                idItem = id,
                isChecked = isChecked
            )

            if (isGoalNew(goal.statusEnum))
                goalRepository.setGoalAsInProgress(goal.id)
        }
    }

    private fun getSelectedItem(goal: Goal, itemDetail: ItemDetail) =
        goal.items.first { it.id == itemDetail.id }

    private fun isGoalNew(statusEnum: StatusEnum) = statusEnum == StatusEnum.NEW
}