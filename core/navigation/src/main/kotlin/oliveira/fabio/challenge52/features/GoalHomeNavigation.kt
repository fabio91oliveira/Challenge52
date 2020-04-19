package oliveira.fabio.challenge52.features

import android.content.Context
import android.content.Intent

interface GoalHomeNavigation {
    fun navigateToFeature(context: Context): Intent
}