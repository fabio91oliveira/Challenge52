package oliveira.fabio.challenge52.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.mapper.DetailsMapper
import oliveira.fabio.challenge52.domain.model.Week
import oliveira.fabio.challenge52.domain.usecase.MountGoalsDetailsUseCase
import java.util.ArrayList

internal class MountGoalsDetailsUseCaseImpl(
    private val detailsMapper: DetailsMapper
) : MountGoalsDetailsUseCase {
    override suspend operator fun invoke(weeks: ArrayList<Week>) =
        withContext(Dispatchers.Default) {
            detailsMapper(weeks)
        }
}