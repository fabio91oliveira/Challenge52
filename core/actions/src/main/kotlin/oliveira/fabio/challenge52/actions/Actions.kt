package oliveira.fabio.challenge52.actions

import android.content.Context
import android.content.Intent

object Actions {
    fun openHome(context: Context): Intent = internalIntent(context, "oliveira.fabio.challenge52.home")
    fun openGoalCreate(context: Context): Intent = internalIntent(context, "oliveira.fabio.challenge52.goalcreate")
    fun openGoalCreateCalendar(context: Context): Intent =
        internalIntent(context, "oliveira.fabio.challenge52.goalcreate.calendar")
    fun openGoalDetails(context: Context): Intent = internalIntent(context, "oliveira.fabio.challenge52.goaldetails")
    private fun internalIntent(context: Context, action: String) = Intent(action).setPackage(context.packageName)
}