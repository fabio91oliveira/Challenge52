package oliveira.fabio.challenge52.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.model.Goal
import oliveira.fabio.challenge52.domain.usecase.CreateTopDetailsUseCase
import oliveira.fabio.challenge52.extensions.toCurrency
import oliveira.fabio.challenge52.presentation.vo.TopDetails

internal class CreateTopDetailsUseCaseImpl() : CreateTopDetailsUseCase {
    override suspend fun invoke(goal: Goal) = withContext(Dispatchers.Default) {
        TopDetails(
            goal.getTotalWeeks(),
            goal.getTotalPercent(),
            goal.getMoneySaved().toCurrency(),
            goal.moneyToSave.toCurrency()
        )
    }
}