package oliveira.fabio.challenge52.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.mapper.ItemMapper
import oliveira.fabio.challenge52.domain.model.Goal
import oliveira.fabio.challenge52.domain.usecase.MountWeeksListUseCase

internal class MountWeeksListUseCaseImpl(
    private val itemMapper: ItemMapper
) : MountWeeksListUseCase {
    override suspend operator fun invoke(goal: Goal) =
        withContext(Dispatchers.Default) {
            itemMapper(goal)
        }
}