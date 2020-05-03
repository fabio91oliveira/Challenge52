package oliveira.fabio.challenge52.goal.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import features.newgoal.R
import kotlinx.android.synthetic.main.fragment_goal_choose_name.*
import oliveira.fabio.challenge52.goal.presentation.action.GoalChooseNameActions
import oliveira.fabio.challenge52.goal.presentation.activity.CreateGoalActivity
import oliveira.fabio.challenge52.goal.presentation.viewmodel.GoalChooseNameViewModel
import oliveira.fabio.challenge52.extensions.setRipple
import oliveira.fabio.challenge52.presentation.dialogfragment.FullScreenDialog
import oliveira.fabio.challenge52.presentation.vo.GoalToSave
import org.koin.androidx.viewmodel.ext.android.getStateViewModel

internal class GoalChooseNameFragment : Fragment(R.layout.fragment_goal_choose_name) {

    private lateinit var goalChooseNameViewModel: GoalChooseNameViewModel

    private val goalName: String
        get() = edtName.text.toString()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setupViewModel()
        setupSelectButtonRipple()
        setupClickListeners()
        setupListeners()
        setupObservables()
        goalChooseNameViewModel.setNameGoal()
    }

    private fun setupViewModel() {
        goalChooseNameViewModel = getStateViewModel(bundle = arguments)
    }

    private fun setupSelectButtonRipple() {
        btnContinue.setRipple(android.R.color.white)
    }

    private fun setupClickListeners() {
        btnContinue.setOnClickListener {
            goalChooseNameViewModel.goToNextStep()
        }
    }

    private fun setupListeners() {
        edtName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateGoalName()
            }

        })
    }

    private fun setupObservables() {
        with(goalChooseNameViewModel) {
            goalChooseNameViewState.observe(
                viewLifecycleOwner,
                Observer {
                    enableBtnContinue(it.isContinueButtonEnabled)
                })
            goalChooseNameActions.observe(viewLifecycleOwner,
                Observer {
                    when (it) {
                        is GoalChooseNameActions.GoToCreateGoalScreen -> {
                            goToCreateGoalScreen(it.goalToSave)
                        }
                        is GoalChooseNameActions.GoToDefinePeriodScreen -> {

                        }
                        is GoalChooseNameActions.SetNameGoal -> {
                            edtName.setText(it.goalName)
                        }
                        is GoalChooseNameActions.CriticalError -> {
                            showFullScreenDialog(
                                it.titleRes,
                                it.descriptionRes
                            )
                        }
                    }
                })
        }
    }

    private fun validateGoalName() {
        goalChooseNameViewModel.isValidName(goalName)
    }

    private fun enableBtnContinue(isEnabled: Boolean) {
        btnContinue.isEnabled = isEnabled
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
                    activity?.finish()
                }
            })
            .setupConfirmButton(
                R.string.all_button_ok,
                object : FullScreenDialog.FullScreenDialogConfirmListener {
                    override fun onClickConfirmButton() {
                        activity?.finish()
                    }
                })
            .setupCancelButton(
                R.string.all_button_go_back,
                object : FullScreenDialog.FullScreenDialogCancelListener {
                    override fun onClickCancelButton() {
                        activity?.finish()
                    }
                })
            .build()
            .show(childFragmentManager, FullScreenDialog.TAG)
    }

    private fun goToCreateGoalScreen(goalToSave: GoalToSave) {
        context?.also {
            startActivity(
                CreateGoalActivity.newIntent(it)
                    .addFlags(
                        Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    ).putExtra(GOAL, goalToSave)
            )
        }
    }

    companion object {
        private const val GOAL = "goal"
    }
}