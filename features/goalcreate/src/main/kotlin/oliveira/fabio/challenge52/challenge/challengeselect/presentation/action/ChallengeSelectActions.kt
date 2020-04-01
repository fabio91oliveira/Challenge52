package oliveira.fabio.challenge52.challenge.challengeselect.presentation.action

import androidx.annotation.StringRes
import oliveira.fabio.challenge52.domain.vo.Challenge

internal sealed class ChallengeSelectActions {
    data class Challenges(val challenges: List<Challenge>) : ChallengeSelectActions()
    data class Error(
        @StringRes val titleMessageRes: Int,
        @StringRes val errorMessageRes: Int
    ) :
        ChallengeSelectActions()
}