package oliveira.fabio.challenge52.feature.targetlist.ui.activity

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_target_list.*
import oliveira.fabio.challenge52.R
import oliveira.fabio.challenge52.feature.targetcreate.ui.activity.TargetCreateActivity

class TargetListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target_list)
        init()
    }

    private fun init() {
        setupToolbar()
        initClickListener()
        initAnimationsNoTargets()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = resources.getString(R.string.target_List_my_targets)
    }

    private fun initClickListener() {
        fab.setOnClickListener { openTargetCreateActivity() }
    }

    private fun initAnimationsNoTargets() {
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = DecelerateInterpolator()
        fadeIn.duration = 2000

        val animation = AnimationSet(false) //change to false
        animation.addAnimation(fadeIn)
        txtNoTargetsFirst.animation = animation
        imgNoTargets.animation = animation

        val valueAnimator = ValueAnimator.ofFloat(-100f, 0f)
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.duration = 1000
        valueAnimator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Float
            txtNoTargetsFirst.translationY = progress
            imgNoTargets.translationY = progress
        }
        valueAnimator.start()
    }

    private fun openTargetCreateActivity() = startActivity(Intent(this, TargetCreateActivity::class.java))


}
