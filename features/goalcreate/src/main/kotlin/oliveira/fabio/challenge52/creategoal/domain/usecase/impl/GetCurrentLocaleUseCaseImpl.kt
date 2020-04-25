package oliveira.fabio.challenge52.creategoal.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.creategoal.domain.usecase.GetCurrentLocaleUseCase
import java.util.*

internal class GetCurrentLocaleUseCaseImpl : GetCurrentLocaleUseCase {
    override suspend fun invoke(): Locale = withContext(Dispatchers.Default) {
        Locale.getDefault()
    }
}