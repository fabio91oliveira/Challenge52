package oliveira.fabio.challenge52

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import oliveira.fabio.challenge52.features.HomeNavigation
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val homeNavigation: HomeNavigation by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        startActivity(homeNavigation.navigateToFeature(this))
        finish()
    }
}
