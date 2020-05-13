package oliveira.fabio.challenge52.domain.usecase

import java.util.*

interface GetCurrentLocaleUseCase {
    suspend operator fun invoke(): Locale
}