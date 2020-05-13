package oliveira.fabio.challenge52.organizer.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.organizer.domain.usecase.GoToPreviousDateUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.ResetDateUseCase
import java.util.*

internal class ResetDateUseCaseImpl(
) : ResetDateUseCase {
    override suspend fun invoke(date: Calendar) = withContext(Dispatchers.Default) {
        date.time = Calendar.getInstance().time
    }
}