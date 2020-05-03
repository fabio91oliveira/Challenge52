package oliveira.fabio.challenge52.goal.domain.usecase

import java.util.*

interface GetCurrentLocaleUseCase {
    suspend operator fun invoke(): Locale
}