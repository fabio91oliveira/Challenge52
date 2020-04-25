package oliveira.fabio.challenge52.domain.usecase

interface RemoveGoalUseCase {
    suspend operator fun invoke(idGoal: Long)
}