package oliveira.fabio.challenge52.presentation.navigation

import android.content.Context
import android.content.Intent
import oliveira.fabio.challenge52.challenge.selectchallenge.presentation.activity.SelectChallengeActivity
import oliveira.fabio.challenge52.features.NewGoalNavigation

internal class NewGoalNavigationImpl : NewGoalNavigation {
    override fun navigateToChallengeSelect(context: Context): Intent =
        SelectChallengeActivity.newIntent(context).addFlags(
            Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        )
}