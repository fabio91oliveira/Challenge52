package oliveira.fabio.challenge52

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import core.base.R

open class BaseActivity : AppCompatActivity() {

    private var onStartCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onStartCount = 1
        if (savedInstanceState == null) {
            this.overridePendingTransition(
                R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_left
            )
        } else {
            onStartCount = 2
        }
    }


    override fun onStart() {
        super.onStart()
        if (onStartCount > 1) {
            this.overridePendingTransition(
                R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right
            )

        } else if (onStartCount == 1) {
            onStartCount++
        }

    }
}