package oliveira.fabio.challenge52

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import oliveira.fabio.challenge52.features.GoalHomeNavigation
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val goalHomeNavigation: GoalHomeNavigation by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        startActivity(goalHomeNavigation.navigateToFeature(this))
        finish()
    }
}
