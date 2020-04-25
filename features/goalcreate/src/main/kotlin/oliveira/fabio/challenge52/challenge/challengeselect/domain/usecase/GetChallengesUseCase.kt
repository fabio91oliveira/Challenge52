package oliveira.fabio.challenge52.challenge.challengeselect.domain.usecase

import oliveira.fabio.challenge52.presentation.vo.Challenge

interface GetChallengesUseCase {
    suspend operator fun invoke(): List<Challenge>
}