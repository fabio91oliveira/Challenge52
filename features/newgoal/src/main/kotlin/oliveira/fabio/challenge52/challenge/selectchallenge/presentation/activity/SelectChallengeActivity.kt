package oliveira.fabio.challenge52.challenge.selectchallenge.presentation.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import features.newgoal.R
import kotlinx.android.synthetic.main.activity_selenct_challenge.*
import oliveira.fabio.challenge52.BaseActivity
import oliveira.fabio.challenge52.challenge.challengeoverview.presentation.activity.ChallengeOverviewActivity
import oliveira.fabio.challenge52.challenge.selectchallenge.presentation.action.ChallengeSelectActions
import oliveira.fabio.challenge52.challenge.selectchallenge.presentation.adapter.ChallengeAdapter
import oliveira.fabio.challenge52.challenge.selectchallenge.presentation.viewmodel.ChallengeSelectViewModel
import oliveira.fabio.challenge52.challenge.selectchallenge.presentation.vo.Challenge
import oliveira.fabio.challenge52.extensions.isVisible
import oliveira.fabio.challenge52.presentation.dialogfragment.FullScreenDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectChallengeActivity : BaseActivity(R.layout.activity_selenct_challenge),
    ChallengeAdapter.ChallengeSelectListener {

    private val challengeAdapter by lazy {
        ChallengeAdapter(
            this
        )
    }
    private val challengeSelectViewModel: ChallengeSelectViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onChallengeClick(challenge: Challenge) {
        startActivity(
            ChallengeOverviewActivity.newIntent(this)
                .addFlags(
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                ).putExtra(CHALLENGE, challenge)
        )
    }

    private fun init() {
        setupToolbar()
        setupRecyclerView()
        setupObservables()
    }

    private fun setupToolbar() {
        with(toolbar) {
            setSupportActionBar(this)
            setNavigationOnClickListener {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    private fun setupRecyclerView() {
        with(rvChallenges) {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                context,
                androidx.recyclerview.widget.RecyclerView.VERTICAL,
                false
            )
            adapter = challengeAdapter
        }
    }

    private fun setupObservables() {
        with(challengeSelectViewModel) {
            challengeSelectViewState.observe(this@SelectChallengeActivity, Observer {
                showChallenges(it.isChallengesVisible)
                showLoading(it.isLoading)
            })
            challengeSelectActions.observe(this@SelectChallengeActivity, Observer {
                when (it) {
                    is ChallengeSelectActions.Challenges -> {
                        challengeAdapter.addChallenges(it.challenges)
                    }
                    is ChallengeSelectActions.Error -> {
                        showFullScreenDialog(it.titleMessageRes, it.errorMessageRes)
                    }
                }
            })
        }
    }

    private fun showLoading(hasToShow: Boolean) {
        loading.isVisible = hasToShow
    }

    private fun showChallenges(hasToShow: Boolean) {
        rvChallenges.isVisible = hasToShow
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

    companion object {
        private const val CHALLENGE = "challenge"

        fun newIntent(context: Context) = Intent(
            context,
            SelectChallengeActivity::class.java
        )
    }

}