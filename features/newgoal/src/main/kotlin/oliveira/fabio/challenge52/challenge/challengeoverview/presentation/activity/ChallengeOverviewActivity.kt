package oliveira.fabio.challenge52.challenge.challengeoverview.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import features.newgoal.R
import kotlinx.android.synthetic.main.activity_challenge_overview.*
import oliveira.fabio.challenge52.BaseActivity
import oliveira.fabio.challenge52.challenge.challengeoverview.presentation.action.ChallengeOverviewActions
import oliveira.fabio.challenge52.challenge.challengeoverview.presentation.adapter.OverviewDetailsAdapter
import oliveira.fabio.challenge52.challenge.challengeoverview.presentation.viewmodel.ChallengeOverviewViewModel
import oliveira.fabio.challenge52.goal.presentation.activity.GoalNameActivity
import oliveira.fabio.challenge52.challenge.selectchallenge.presentation.vo.Challenge
import oliveira.fabio.challenge52.extensions.isVisible
import oliveira.fabio.challenge52.extensions.setRipple
import oliveira.fabio.challenge52.presentation.dialogfragment.FullScreenDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

internal class ChallengeOverviewActivity : BaseActivity(R.layout.activity_challenge_overview) {

    private val challenge by lazy {
        intent.extras?.getParcelable(CHALLENGE) as Challenge
    }

    private val challengeOverviewViewModel: ChallengeOverviewViewModel by viewModel {
        parametersOf(
            challenge
        )
    }

    private val overviewDetailsAdapter by lazy {
        OverviewDetailsAdapter()
    }

    private val indicators by lazy {
        arrayOf<AppCompatImageView>(imgIndicatorOne, imgIndicatorTwo, imgIndicatorThree)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        setupToolbar()
        setupViewPager()
        setupSelectButtonRipple()
        setupObservables()
        setupListeners()
    }

    private fun setupToolbar() {
        with(toolbar) {
            setSupportActionBar(this)
            supportActionBar?.title = challenge.name
            setNavigationOnClickListener { finish() }
        }
    }

    private fun setupObservables() {
        with(challengeOverviewViewModel) {
            challengeOverviewViewState.observe(this@ChallengeOverviewActivity, Observer {
                showLoading(it.isLoading)
            })
            challengeOverviewActions.observe(this@ChallengeOverviewActivity, Observer {
                when (it) {
                    is ChallengeOverviewActions.ShowScreens -> {
                        overviewDetailsAdapter.addOverviewDetails(it.list)
                    }
                    is ChallengeOverviewActions.Error -> {
                        showFullScreenDialog(it.resTitle, it.resDescription)
                    }
                }
            })
        }
    }

    private fun setupViewPager() {
        with(viewPager) {
            adapter = overviewDetailsAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    updateIndicators(position)
                    btnSkip.visibility =
                        getVisibility(position != overviewDetailsAdapter.getFinalPage())
                    btnNext.visibility =
                        getVisibility(position != overviewDetailsAdapter.getFinalPage())
                    btnConfirm.visibility =
                        getVisibility(position == overviewDetailsAdapter.getFinalPage())
                }
            })
        }
    }

    private fun setupSelectButtonRipple() {
        btnConfirm.setRipple(android.R.color.white)
    }

    private fun setupListeners() {
        btnSkip.setOnClickListener {
            skip()
        }
        btnNext.setOnClickListener {
            goNext()
        }
        btnConfirm.setOnClickListener {
            openGoalName()
        }
    }

    private fun updateIndicators(position: Int) {
        for (i in indicators.indices) {
            indicators[i].setBackgroundResource(
                if (i == position) R.drawable.indicator_selected else R.drawable.indicator_unselected
            )
        }
    }

    private fun showLoading(hasToShow: Boolean) {
        loading.isVisible = hasToShow
    }

    private fun skip() {
        viewPager.currentItem = overviewDetailsAdapter.getFinalPage()
    }

    private fun goNext() {
        viewPager.currentItem = viewPager.currentItem + 1
    }

    private fun showFullScreenDialog(@StringRes titleRes: Int, @StringRes descriptionRes: Int) {
        FullScreenDialog.Builder()
            .setTitle(titleRes)
            .setSubtitle(descriptionRes)
            .setCloseAction(object : FullScreenDialog.FullScreenDialogCloseListener {
                override fun onClickCloseButton() {
                    finish()
                }
            })
            .setupConfirmButton(
                R.string.all_button_ok,
                object : FullScreenDialog.FullScreenDialogConfirmListener {
                    override fun onClickConfirmButton() {
                        finish()
                    }
                })
            .setupCancelButton(
                R.string.all_button_go_back,
                object : FullScreenDialog.FullScreenDialogCancelListener {
                    override fun onClickCancelButton() {
                        finish()
                    }
                })
            .build()
            .show(supportFragmentManager, FullScreenDialog.TAG)
    }

    private fun getVisibility(isVisible: Boolean) = if (isVisible) View.VISIBLE else View.INVISIBLE

    private fun openGoalName() =
        startActivity(
            GoalNameActivity.newIntent(this)
                .addFlags(
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                ).putExtra(CHALLENGE, challenge)
        )

    companion object {
        private const val CHALLENGE = "challenge"

        fun newIntent(context: Context) = Intent(
            context,
            ChallengeOverviewActivity::class.java
        )
    }
}