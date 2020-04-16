package oliveira.fabio.challenge52.creategoal.domain.usecase

import java.util.*

interface GetCurrentLocaleUseCase {
    suspend operator fun invoke(): Locale
}