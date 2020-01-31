package oliveira.fabio.challenge52

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import oliveira.fabio.challenge52.actions.Actions

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        startActivity(Actions.openHome(this))
        finish()
    }
}
