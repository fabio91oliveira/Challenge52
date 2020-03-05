package oliveira.fabio.challenge52

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import core.base.R

open class BaseActivity : AppCompatActivity {
    constructor()
    constructor(@LayoutRes layoutRes: Int) : super(layoutRes)

    private var onStartCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.also {
            onStartCount = 2
        } ?: run {
            this.overridePendingTransition(
                R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_left
            )
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