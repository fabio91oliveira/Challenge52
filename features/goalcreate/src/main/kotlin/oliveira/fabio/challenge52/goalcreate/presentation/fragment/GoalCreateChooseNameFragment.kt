package oliveira.fabio.challenge52.goalcreate.presentation.fragment

import android.animation.ValueAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import features.goalcreate.R
import kotlinx.android.synthetic.main.fragment_goal_create_choose_name.*
import oliveira.fabio.challenge52.domain.vo.Challenge
import oliveira.fabio.challenge52.extensions.setRipple
import oliveira.fabio.challenge52.goalcreate.domain.vo.GoalSuggestion
import oliveira.fabio.challenge52.goalcreate.presentation.viewmodel.GoalCreateChooseNameViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class GoalCreateChooseNameFragment : Fragment(R.layout.fragment_goal_create_choose_name) {

    private val goalSuggestion by lazy { arguments?.getParcelable(GOAL_SUGGESTION) as GoalSuggestion }
    private val challenge by lazy {
        arguments?.getParcelable<Challenge>(CHALLENGE)
    }
    private val goalCreateChooseNameViewModel: GoalCreateChooseNameViewModel by viewModel()

    private val goalName: String
        get() = edtName.text.toString()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setupScreen()
        setupSelectButtonRipple()
        setupClickListeners()
        setupListeners()
        setupObservables()
        validateGoalName()
    }

    private fun setupScreen() {
        edtName.setText(goalSuggestion.name)
    }

    private fun setupSelectButtonRipple() {
        btnBack.setRipple(android.R.color.white)
        btnContinue.setRipple(android.R.color.white)
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
        btnContinue.setOnClickListener {
            findNavController().navigate(
                R.id.action_go_to_fragment_goal_create_set_periods
            )
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
        goalCreateChooseNameViewModel.goalCreateChooseNameViewState.observe(
            viewLifecycleOwner,
            Observer {
                enableBtnContinue(it.isContinueButtonEnabled)
            })
    }

    private fun validateGoalName() {
        goalCreateChooseNameViewModel.isValidName(goalName)
    }

    private fun enableBtnContinue(isEnabled: Boolean) {
        btnContinue.isEnabled = isEnabled
    }

//    private fun initAnimations() {
//        val valueAnimator = ValueAnimator.ofFloat(-700f, 0f)
//        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
//        valueAnimator.duration = 800
//        valueAnimator.addUpdateListener {
//            val progress = it.animatedValue as Float
//            viewTop.translationY = progress
//        }
//        valueAnimator.start()
//
//        val valueAnimator2 = ValueAnimator.ofFloat(-700f, 0f)
//        valueAnimator2.interpolator = AccelerateDecelerateInterpolator()
//        valueAnimator2.duration = 800
//        valueAnimator2.addUpdateListener {
//            val progress = it.animatedValue as Float
//            txtTitle?.translationY = progress
//        }
//        valueAnimator2.start()
//
//        val valueAnimator3 = ValueAnimator.ofFloat(1700f, 0f)
//        valueAnimator3.interpolator = AccelerateDecelerateInterpolator()
//        valueAnimator3.duration = 400
//        valueAnimator3.addUpdateListener {
//            val progress = it.animatedValue as Float
//            tilName.translationY = progress
//            tilName.translationZ = progress
//
//        }
//        valueAnimator3.start()
//    }

    companion object {
        private const val GOAL_SUGGESTION = "goal_suggestion"
        private const val CHALLENGE = "challenge"
    }
}