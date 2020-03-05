package oliveira.fabio.challenge52.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.mapper.WeekDetailsMapper
import oliveira.fabio.challenge52.domain.model.Week
import oliveira.fabio.challenge52.domain.usecase.MountWeeksListUseCase
import java.util.ArrayList

internal class MountWeeksListUseCaseImpl(
    private val weekDetailsMapper: WeekDetailsMapper
) : MountWeeksListUseCase {
    override suspend operator fun invoke(weeks: ArrayList<Week>) =
        withContext(Dispatchers.Default) {
            weekDetailsMapper(weeks)
        }
}