package oliveira.fabio.challenge52.creategoal.domain.usecase

import oliveira.fabio.challenge52.creategoal.presentation.vo.MoneySuggestion

interface GetMoneySuggestionsUseCase {
    suspend operator fun invoke(): List<MoneySuggestion>
}