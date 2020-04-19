package oliveira.fabio.challenge52.features

import android.content.Context
import android.content.Intent

interface GoalDetailsNavigation {
    fun navigateToFeature(context: Context): Intent
}