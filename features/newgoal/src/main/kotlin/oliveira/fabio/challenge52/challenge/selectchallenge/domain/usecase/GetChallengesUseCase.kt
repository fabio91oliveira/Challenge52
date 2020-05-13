package oliveira.fabio.challenge52.challenge.selectchallenge.domain.usecase

import oliveira.fabio.challenge52.challenge.selectchallenge.presentation.vo.Challenge

interface GetChallengesUseCase {
    suspend operator fun invoke(): List<Challenge>
}