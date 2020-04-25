package oliveira.fabio.challenge52.creategoal.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.creategoal.domain.usecase.AddItemsUseCase
import oliveira.fabio.challenge52.domain.repository.ItemRepository
import oliveira.fabio.challenge52.presentation.vo.GoalToSave

internal class AddItemsUseCaseImpl(
    private val itemRepository: ItemRepository
) : AddItemsUseCase {
    override suspend fun invoke(
        goalToSave: GoalToSave,
        idGoal: Long
    ) =
        withContext(Dispatchers.IO) {
            itemRepository.addItems(goalToSave, idGoal)
        }
}