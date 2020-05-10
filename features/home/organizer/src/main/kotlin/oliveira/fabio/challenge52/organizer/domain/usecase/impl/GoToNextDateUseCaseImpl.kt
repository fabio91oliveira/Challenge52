package oliveira.fabio.challenge52.organizer.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.organizer.domain.usecase.GoToNextDateUseCase
import java.util.*

internal class GoToNextDateUseCaseImpl(
) : GoToNextDateUseCase {
    override suspend fun invoke(date: Calendar) = withContext(Dispatchers.Default) {
        date.add(Calendar.MONTH, 1)
    }
}