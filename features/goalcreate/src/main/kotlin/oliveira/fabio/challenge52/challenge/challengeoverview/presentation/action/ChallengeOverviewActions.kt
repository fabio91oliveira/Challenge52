package oliveira.fabio.challenge52.challenge.challengeoverview.presentation.action

import androidx.annotation.StringRes
import oliveira.fabio.challenge52.challenge.challengeoverview.presentation.vo.OverviewDetails

internal sealed class ChallengeOverviewActions {
    data class ShowScreens(
        val list: List<OverviewDetails>
    ) : ChallengeOverviewActions()

    data class Error(
        @StringRes val resTitle: Int,
        @StringRes val resDescription: Int
    ) : ChallengeOverviewActions()
}