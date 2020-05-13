package oliveira.fabio.challenge52.goal.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import features.newgoal.R
import kotlinx.android.synthetic.main.activity_create_goal.*
import oliveira.fabio.challenge52.BaseActivity
import oliveira.fabio.challenge52.extensions.setRipple
import oliveira.fabio.challenge52.extensions.toCurrencyAndTextChangeAction
import oliveira.fabio.challenge52.extensions.toStringMoney
import oliveira.fabio.challenge52.goal.presentation.action.CreateGoalActions
import oliveira.fabio.challenge52.goal.presentation.adapter.MoneySuggestionAdapter
import oliveira.fabio.challenge52.goal.presentation.viewmodel.CreateGoalViewModel
import oliveira.fabio.challenge52.goal.presentation.vo.MoneySuggestion
import oliveira.fabio.challenge52.presentation.dialogfragment.FullScreenDialog
import org.koin.androidx.viewmodel.ext.android.getStateViewModel

internal class CreateGoalActivity :
    BaseActivity(R.layout.activity_create_goal),
    MoneySuggestionAdapter.MoneySuggestionSuggestionClickListener {

    private val moneyAdapter by lazy { MoneySuggestionAdapter(this) }

    private val money: String
        get() = edtMoney.text.toString()

    private lateinit var createGoalViewModel: CreateGoalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    override fun onDateSuggestionClick(moneySuggestion: MoneySuggestion) {
        setPeriodMoney(moneySuggestion.moneyPresentation)
    }

    private fun init() {
        setupViewModel()
        setupToolbar()
        setupSelectButtonRipple()
        setupClickListener()
        setupEditText()
        setupEditTextsListeners()
        setupObservables()
    }

    private fun setupViewModel() {
        createGoalViewModel = getStateViewModel(bundle = intent.extras)
    }

    private fun setupToolbar() {
        with(toolbar) {
            setSupportActionBar(this)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            setNavigationOnClickListener { finish() }
        }
    }

    private fun setupSelectButtonRipple() {
        btnCreate.setRipple(android.R.color.white)
    }

    private fun setupClickListener() {
        btnCreate.setOnClickListener {
            createGoalViewModel.createGoal()
        }
    }

    private fun setupEditText() {
        val hint = 0.toDouble().toStringMoney()
        edtMoney.hint = hint
        edtMoney.requestFocus()
    }

    private fun setupEditTextsListeners() {
        edtMoney.toCurrencyAndTextChangeAction {
            createGoalViewModel.calculateTotalMoney(money)
        }
    }

    private fun setupObservables() {
        with(createGoalViewModel) {
            createGoalActions.observe(this@CreateGoalActivity, Observer {
                when (it) {
                    is CreateGoalActions.ShowMoneySuggestions -> {
                        moneyAdapter.addSuggestions(it.moneySuggestions)
                    }
                    is CreateGoalActions.GoalCreated -> {
                        finishAffinity()
                    }
                    is CreateGoalActions.CriticalError -> {
                        showFullScreenDialog(
                            it.titleRes,
                            it.descriptionRes
                        )
                    }
                }
            })
            createGoalViewState.observe(this@CreateGoalActivity, Observer {
                setPeriodType(it.periodType)
                setTotalMoney(it.totalMoney)
                enableFinishButton(it.isCreateButtonEnable)
            })
        }
    }

    private fun setPeriodType(@StringRes periodType: Int) {
        txtTitle.text = getString(periodType)
    }

    private fun setTotalMoney(totalMoney: String) {
        txtTotalMoney.text = totalMoney
    }

    private fun setPeriodMoney(money: String) = edtMoney.setText(money)

    private fun enableFinishButton(isFinishButtonEnabled: Boolean) {
        btnCreate.isEnabled = isFinishButtonEnabled
    }

    private fun showFullScreenDialog(
        @StringRes titleRes: Int,
        @StringRes descriptionRes: Int
    ) {
        FullScreenDialog.Builder()
            .setTitle(titleRes)
            .setSubtitle(descriptionRes)
            .setCloseAction(object : FullScreenDialog.FullScreenDialogCloseListener {
                override fun onClickCloseButton() {
                    finishAffinity()
                }
            })
            .setupConfirmButton(
                R.string.all_button_ok,
                object : FullScreenDialog.FullScreenDialogConfirmListener {
                    override fun onClickConfirmButton() {
                        finishAffinity()
                    }
                })
            .setupCancelButton(
                R.string.all_button_go_back,
                object : FullScreenDialog.FullScreenDialogCancelListener {
                    override fun onClickCancelButton() {
                        finishAffinity()
                    }
                })
            .build()
            .show(supportFragmentManager, FullScreenDialog.TAG)
    }

    companion object {
        fun newIntent(context: Context) = Intent(
            context,
            CreateGoalActivity::class.java
        )
    }
}