package oliveira.fabio.challenge52.main.presentation.navigation

import android.content.Context
import android.content.Intent
import oliveira.fabio.challenge52.features.HomeNavigation
import oliveira.fabio.challenge52.main.presentation.activity.HomeActivity

internal class HomeNavigationImpl : HomeNavigation {
    override fun navigateToFeature(context: Context): Intent =
        HomeActivity.newIntent(context).addFlags(
            Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        )
}