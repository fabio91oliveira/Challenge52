package oliveira.fabio.challenge52.presentation.navigation

import android.content.Context
import android.content.Intent
import oliveira.fabio.challenge52.features.GoalDetailsNavigation
import oliveira.fabio.challenge52.presentation.activity.GoalDetailsActivity

internal class GoalDetailsNavigationImpl : GoalDetailsNavigation {
    override fun navigateToFeature(context: Context): Intent =
        GoalDetailsActivity.newIntent(context).addFlags(
            Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        )
}