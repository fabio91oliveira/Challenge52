package oliveira.fabio.challenge52.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.mapper.WeekMapper
import oliveira.fabio.challenge52.domain.repository.WeekRepository
import oliveira.fabio.challenge52.domain.usecase.AddWeeksUseCase
import oliveira.fabio.challenge52.persistence.model.entity.Goal
import oliveira.fabio.challenge52.persistence.model.entity.Week
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

class AddWeeksUseCaseImpl(
    private val weekRepository: WeekRepository,
    private val weekMapper: WeekMapper
) : AddWeeksUseCase {
    override suspend operator fun invoke(goal: Goal, id: Long) =
        weekRepository.addWeeks(createWeeks(goal, id))

    private suspend fun createWeeks(goal: Goal, id: Long) = withContext(Dispatchers.Main) {
        weekMapper(goal, id)
    }
}