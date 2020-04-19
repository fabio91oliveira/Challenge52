package oliveira.fabio.challenge52.presentation.navigation

import android.content.Context
import android.content.Intent
import oliveira.fabio.challenge52.challenge.challengeselect.presentation.activity.ChallengeSelectActivity
import oliveira.fabio.challenge52.features.CreateGoalNavigation

internal class CreateGoalNavigationImpl : CreateGoalNavigation {
    override fun navigateToChallengeSelect(context: Context): Intent =
        ChallengeSelectActivity.newIntent(context).addFlags(
            Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        )
}