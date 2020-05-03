package oliveira.fabio.challenge52.features

import android.content.Context
import android.content.Intent

interface NewGoalNavigation {
    fun navigateToChallengeSelect(context: Context): Intent
}