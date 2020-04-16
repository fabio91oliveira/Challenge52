package oliveira.fabio.challenge52.creategoal.presentation.activity

import android.app.Activity
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import features.goalcreate.R
import kotlinx.android.synthetic.main.activity_create_goal_final_step.*
import oliveira.fabio.challenge52.BaseActivity
import oliveira.fabio.challenge52.creategoal.presentation.action.CreateGoalFinalStepActions
import oliveira.fabio.challenge52.creategoal.presentation.adapter.MoneySuggestionAdapter
import oliveira.fabio.challenge52.creategoal.presentation.viewmodel.CreateGoalFinalStepViewModel
import oliveira.fabio.challenge52.creategoal.presentation.vo.MoneySuggestion
import oliveira.fabio.challenge52.extensions.setRipple
import oliveira.fabio.challenge52.extensions.toCurrency
import oliveira.fabio.challenge52.extensions.toCurrencyAndTextChangeAction
import oliveira.fabio.challenge52.presentation.dialogfragment.FullScreenDialog
import org.koin.androidx.viewmodel.ext.android.getStateViewModel

internal class CreateGoalFinalStepActivity :
    BaseActivity(R.layout.activity_create_goal_final_step),
    MoneySuggestionAdapter.MoneySuggestionSuggestionClickListener {

    private val moneyAdapter by lazy { MoneySuggestionAdapter(this) }

    private val money: String
        get() = edtMoney.text.toString()

    private lateinit var createGoalFinalStepViewModel: CreateGoalFinalStepViewModel

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
        setupEditTextHint()
        setupEditTextsListeners()
        setupRecyclerView()
        setupObservables()
    }

    private fun setupViewModel() {
        createGoalFinalStepViewModel = getStateViewModel(bundle = intent.extras)
    }

    private fun setupToolbar() {
        with(toolbar) {
            setSupportActionBar(this)
            setNavigationOnClickListener { finish() }
        }
    }

    private fun setupSelectButtonRipple() {
        btnCreate.setRipple(android.R.color.white)
    }

    private fun setupClickListener() {
        btnCreate.setOnClickListener {
            createGoalFinalStepViewModel.createGoal()
        }
    }

    private fun setupEditTextHint() {
        val hint = 0f.toCurrency()
        edtMoney.hint = hint
    }

    private fun setupEditTextsListeners() {
        edtMoney.toCurrencyAndTextChangeAction {
            createGoalFinalStepViewModel.calculateTotalMoney(money)
        }
    }

    private fun setupRecyclerView() {
        with(rvMoneySuggestions) {
            layoutManager =
                androidx.recyclerview.widget.LinearLayoutManager(
                    this@CreateGoalFinalStepActivity,
                    androidx.recyclerview.widget.RecyclerView.HORIZONTAL,
                    false
                )
            adapter = moneyAdapter
            itemAnimator = null
        }
    }

    private fun setupObservables() {
        with(createGoalFinalStepViewModel) {
            createGoalFinalStepActions.observe(this@CreateGoalFinalStepActivity, Observer {
                when (it) {
                    is CreateGoalFinalStepActions.ShowMoneySuggestions -> {
                        moneyAdapter.addSuggestions(it.moneySuggestions)
                    }
                    is CreateGoalFinalStepActions.GoalCreated -> {
                        finishAffinity()
                    }
                    is CreateGoalFinalStepActions.CriticalError -> {
                        showFullScreenDialog(
                            it.titleRes,
                            it.descriptionRes
                        )
                    }
                }
            })
            createGoalFinalStepViewState.observe(this@CreateGoalFinalStepActivity, Observer {
                setPeriodType(it.periodType)
                setTotalMoney(it.totalMoney)
                enableFinishButton(it.isCreateButtonEnable)
            })
        }
    }

    private fun setPeriodType(@StringRes periodType: Int) {
        txtPeriod.setText(periodType)
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
}