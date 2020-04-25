package oliveira.fabio.challenge52.domain.usecase

interface SetGoalAsDoneUseCase {
    suspend operator fun invoke(idGoal: Long)
}