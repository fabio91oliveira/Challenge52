package oliveira.fabio.challenge52.organizer.domain.usecase

interface ChangeHideOptionUseCase {
    suspend operator fun invoke(idBalance: Long, isHide: Boolean)
}