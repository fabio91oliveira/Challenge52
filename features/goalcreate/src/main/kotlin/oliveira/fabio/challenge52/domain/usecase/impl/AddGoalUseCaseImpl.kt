package oliveira.fabio.challenge52.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.mapper.WeekMapper
import oliveira.fabio.challenge52.domain.repository.GoalRepository
import oliveira.fabio.challenge52.domain.repository.WeekRepository
import oliveira.fabio.challenge52.domain.usecase.AddGoalUseCase
import oliveira.fabio.challenge52.extensions.removeMoneyMask
import oliveira.fabio.challenge52.extensions.toDate
import oliveira.fabio.challenge52.extensions.toFloatCurrency
import oliveira.fabio.challenge52.persistence.model.entity.Goal
import java.text.DateFormat

class AddGoalUseCaseImpl(
    private val goalRepository: GoalRepository,
    private val weekRepository: WeekRepository,
    private val weekMapper: WeekMapper
) : AddGoalUseCase {
    override suspend operator fun invoke(
        initialDate: String,
        name: String,
        valueToStart: String
    ) {
        withContext(Dispatchers.IO) {
            Goal(
                initialDate = initialDate.toDate(DateFormat.SHORT),
                name = name,
                valueToStart = valueToStart.removeMoneyMask().toFloatCurrency()
            ).apply {
                val id = goalRepository.addGoal(this)
                weekRepository.addWeeks(weekMapper(this, id))
            }
        }
    }
}
