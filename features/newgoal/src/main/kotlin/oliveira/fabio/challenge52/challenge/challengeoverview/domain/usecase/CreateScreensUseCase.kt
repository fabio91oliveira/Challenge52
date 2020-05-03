package oliveira.fabio.challenge52.challenge.challengeoverview.domain.usecase

import oliveira.fabio.challenge52.challenge.challengeoverview.presentation.vo.OverviewDetails
import oliveira.fabio.challenge52.challenge.selectchallenge.presentation.vo.Challenge

internal interface CreateScreensUseCase {
    suspend operator fun invoke(challenge: Challenge): MutableList<OverviewDetails>
}