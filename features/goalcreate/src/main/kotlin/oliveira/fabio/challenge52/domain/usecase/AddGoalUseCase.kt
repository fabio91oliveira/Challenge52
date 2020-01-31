package oliveira.fabio.challenge52.domain.usecase

interface AddGoalUseCase {
    suspend operator fun invoke(initialDate: String, name: String, valueToStart: String)
}