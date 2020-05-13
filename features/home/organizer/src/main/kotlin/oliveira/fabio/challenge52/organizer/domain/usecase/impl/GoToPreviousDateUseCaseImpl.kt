package oliveira.fabio.challenge52.organizer.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.organizer.domain.usecase.GoToPreviousDateUseCase
import java.util.*

internal class GoToPreviousDateUseCaseImpl(
) : GoToPreviousDateUseCase {
    override suspend fun invoke(date: Calendar) = withContext(Dispatchers.Default) {
        date.add(Calendar.MONTH, -1)
    }
}