package oliveira.fabio.challenge52.goal.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.goal.domain.usecase.GetCurrentLocaleUseCase
import oliveira.fabio.challenge52.goal.domain.usecase.GetMoneySuggestionsUseCase
import oliveira.fabio.challenge52.goal.presentation.vo.MoneySuggestion
import oliveira.fabio.challenge52.extensions.toStringMoney
import java.util.*

internal class GetMoneySuggestionsUseCaseImpl(
    private val getCurrentLocaleUseCase: GetCurrentLocaleUseCase
) : GetMoneySuggestionsUseCase {
    override suspend fun invoke() = withContext(Dispatchers.Default) {
        mutableListOf<MoneySuggestion>().apply {
            val suggestions = arrayListOf(1.0f, 5.0f, 10.0f, 20.0f, 50.0f, 100.0f, 200.0f, 500.0f)

            suggestions.forEach {
                add(
                    MoneySuggestion(
                        currency = Currency.getInstance(getCurrentLocaleUseCase()).symbol,
                        moneyPresentation = it.toStringMoney(useCurrency = false),
                        moneyValue = it
                    )
                )
            }
        }
    }
}