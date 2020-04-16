package oliveira.fabio.challenge52.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.repository.GoalRepository
import oliveira.fabio.challenge52.domain.repository.ItemRepository
import oliveira.fabio.challenge52.domain.usecase.RemoveGoalUseCase

internal class RemoveGoalUseCaseImpl(
    private val goalRepository: GoalRepository,
    private val weekRepository: ItemRepository
) : RemoveGoalUseCase {
    override suspend operator fun invoke(idGoal: Long) {
        withContext(Dispatchers.IO) {
            goalRepository.removeGoal(idGoal)
            weekRepository.removeItemsByIdGoal(idGoal)
        }
    }
}