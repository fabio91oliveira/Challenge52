package oliveira.fabio.challenge52.home.presentation.navigation

import android.content.Context
import android.content.Intent
import oliveira.fabio.challenge52.features.GoalHomeNavigation
import oliveira.fabio.challenge52.home.presentation.activity.HomeActivity

internal class GoalHomeNavigationImpl : GoalHomeNavigation {
    override fun navigateToFeature(context: Context): Intent =
        HomeActivity.newIntent(context).addFlags(
            Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        )
}