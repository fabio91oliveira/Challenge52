package oliveira.fabio.challenge52.feature.targetcreate.ui.activity

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_target_create.*
import oliveira.fabio.challenge52.R


class TargetCreateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target_create)
        init()
    }

    private fun init() {
        setupToolbar()
        initAnimations()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = resources.getString(R.string.target_create_new_target)
        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun initAnimations() {
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = DecelerateInterpolator()
        fadeIn.duration = 1400

        val animation = AnimationSet(false) //change to false
        animation.addAnimation(fadeIn)
        tilName.animation = animation
        tilValue.animation = animation
        tilData.animation = animation
        btnCreate.animation = animation

        val valueAnimator = ValueAnimator.ofFloat(-100f, 0f)
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.duration = 500
        valueAnimator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Float
            card.translationY = progress
        }
        valueAnimator.start()

        val valueAnimator2 = ValueAnimator.ofFloat(-300f, 0f)
        valueAnimator2.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator2.duration = 500
        valueAnimator2.addUpdateListener { animation ->
            val progress = animation.animatedValue as Float
            btnCreate.translationX = progress
        }
        valueAnimator2.start()
    }
}