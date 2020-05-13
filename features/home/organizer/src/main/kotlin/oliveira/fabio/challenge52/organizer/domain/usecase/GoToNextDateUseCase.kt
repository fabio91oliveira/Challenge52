package oliveira.fabio.challenge52.organizer.domain.usecase

import java.util.*

interface GoToNextDateUseCase {
    suspend operator fun invoke(
        date: Calendar
    )
}