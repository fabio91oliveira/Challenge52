package oliveira.fabio.challenge52.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.mapper.DetailsMapper
import oliveira.fabio.challenge52.domain.usecase.MountGoalsDetailsUseCase
import oliveira.fabio.challenge52.presentation.vo.Goal

internal class MountGoalsDetailsUseCaseImpl(
    private val detailsMapper: DetailsMapper
) : MountGoalsDetailsUseCase {
    override suspend operator fun invoke(goal: Goal) =
        withContext(Dispatchers.Default) {
            delay(1000)
            detailsMapper(goal)
        }
}