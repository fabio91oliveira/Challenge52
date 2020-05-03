package oliveira.fabio.challenge52.goal.domain.usecase

import oliveira.fabio.challenge52.goal.presentation.vo.MoneySuggestion

interface GetMoneySuggestionsUseCase {
    suspend operator fun invoke(): List<MoneySuggestion>
}