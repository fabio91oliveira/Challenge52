package oliveira.fabio.challenge52.presentation.ui.activity

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.lifecycle.Observer
import features.goalcreate.R
import kotlinx.android.synthetic.main.activity_goal_create.*
import oliveira.fabio.challenge52.BaseActivity
import oliveira.fabio.challenge52.actions.Actions
import oliveira.fabio.challenge52.di.injectGoalCreateDependencies
import oliveira.fabio.challenge52.extensions.*
import oliveira.fabio.challenge52.persistence.model.entity.Goal
import oliveira.fabio.challenge52.presentation.state.GoalCreateState
import oliveira.fabio.challenge52.presentation.viewmodel.GoalCreateViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.DateFormat
import java.util.*

class GoalCreateActivity : BaseActivity() {

    private val goalCreateViewModel: GoalCreateViewModel by viewModel()
    private val calendar by lazy { Calendar.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CALENDAR) {
            val calendar = data?.getSerializableExtra(CALENDAR_TAG) as Calendar

            with(this.calendar) {
                set(Calendar.YEAR, calendar.get(Calendar.YEAR))
                set(Calendar.MONTH, calendar.get(Calendar.MONTH))
                set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))
                setData(this.toCurrentDateSystemString(DateFormat.SHORT))
            }

            content.requestFocus()
        }
    }

    private fun init() {
        injectGoalCreateDependencies()
        setupView()
        setupToolbar()
        initFields()
        initLiveData()
        initClickListeners()
        initFocusListeners()
        initAnimations()
    }

    private fun initLiveData() {
        with(goalCreateViewModel) {
            goalCreateState.observe(this@GoalCreateActivity, Observer {
                when (it) {
                    GoalCreateState.Success -> {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    GoalCreateState.Error -> {
                        setResult(ACTIVITY_ERROR)
                        finish()
                    }
                }
            })
        }
    }

    private fun setupView() = setContentView(R.layout.activity_goal_create)

    private fun setupToolbar() {
        with(toolbar) {
            setSupportActionBar(this)
            supportActionBar?.title = resources.getString(R.string.goal_create_new_goal)
            setNavigationOnClickListener { finish() }
        }
    }

    private fun setData(date: String) = txtDate.setText(date)

    private fun initClickListeners() {
        tilData.setOnClickListener { openCalendar() }
        txtDate.setOnClickListener { openCalendar() }
        btnCreate.setOnClickListener { goalCreateViewModel.createGoal(getFilledGoal()) }
    }

    private fun initFocusListeners() {
        txtName.setOnFocusChangeListener { _, _ -> validateCreateButton() }
        txtValue.setOnFocusChangeListener { _, _ -> validateCreateButton() }
    }

    private fun initFields() {
        setData(calendar.toCurrentDateSystemString(DateFormat.SHORT))
        txtName.callFunctionAfterTextChanged { validateCreateButton() }
        txtValue.toCurrencyFormat {
            validateCreateButton()
        }
        tilValue.hint = resources.getString(R.string.goal_create_goal_value, MONEY_MIN.toCurrency())
    }

    private fun initAnimations() {
        val valueAnimator = ValueAnimator.ofFloat(-700f, 0f)
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.duration = 800
        valueAnimator.addUpdateListener {
            val progress = it.animatedValue as Float
            card.translationY = progress
        }
        valueAnimator.start()

        val valueAnimator2 = ValueAnimator.ofFloat(1100f, 0f)
        valueAnimator2.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator2.duration = 800
        valueAnimator2.addUpdateListener {
            val progress = it.animatedValue as Float
            btnCreate.translationX = progress
        }
        valueAnimator2.start()
    }

    private fun openCalendar() = startActivityForResult(
        Actions.openGoalCreateCalendar(this).putExtra(CALENDAR_TAG, calendar),
        REQUEST_CODE_CALENDAR
    )

    private fun validateCreateButton() {
        btnCreate.isEnabled =
            goalCreateViewModel.isAllFieldsFilled(txtName.text.toString(), txtValue.text.toString())
    }

    private fun getFilledGoal() = Goal().apply {
        initialDate = txtDate.toDate(DateFormat.SHORT)
        name = txtName.text.toString()
        valueToStart = goalCreateViewModel.getFloatCurrencyValue(txtValue.text.toString())
    }

    companion object {
        private const val MONEY_MIN = 1f
        private const val ACTIVITY_ERROR = -3
        private const val REQUEST_CODE_CALENDAR = 200
        private const val CALENDAR_TAG = "CALENDAR"
    }
}