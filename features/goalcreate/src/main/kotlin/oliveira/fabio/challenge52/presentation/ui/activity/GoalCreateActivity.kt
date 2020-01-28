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
import oliveira.fabio.challenge52.extensions.callFunctionAfterTextChanged
import oliveira.fabio.challenge52.extensions.toCurrency
import oliveira.fabio.challenge52.extensions.toCurrencyFormat
import oliveira.fabio.challenge52.extensions.toCurrentDateSystemString
import oliveira.fabio.challenge52.presentation.action.GoalCreateActions
import oliveira.fabio.challenge52.presentation.viewmodel.GoalCreateViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.DateFormat
import java.util.*

class GoalCreateActivity : BaseActivity(R.layout.activity_goal_create) {

    private val goalCreateViewModel: GoalCreateViewModel by viewModel()
    private val calendarToday by lazy { Calendar.getInstance() }

    private val taskName: String
        get() = txtName.text.toString()
    private val taskValue: String
        get() = txtValue.text.toString()
    private val taskDate: String
        get() = txtDate.text.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CALENDAR) {
            val calendar = data?.getSerializableExtra(CALENDAR_TAG) as Calendar

            with(calendarToday) {
                set(Calendar.YEAR, calendar.get(Calendar.YEAR))
                set(Calendar.MONTH, calendar.get(Calendar.MONTH))
                set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))
                setData(toCurrentDateSystemString(DateFormat.SHORT))
            }

            content.requestFocus()
        }
    }

    private fun init() {
        setupToolbar()
        initFields()
        initObservables()
        initClickListeners()
        initFocusListeners()
        initAnimations()
    }

    private fun initObservables() {
        with(goalCreateViewModel) {
            goalCreateViewState.observe(this@GoalCreateActivity, Observer {
                btnCreate.isEnabled = it.isCreateButtonEnable
            })
            goalCreateActions.observe(this@GoalCreateActivity, Observer {
                when (it) {
                    GoalCreateActions.ShowSuccess -> {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    GoalCreateActions.ShowError -> {
                        setResult(ACTIVITY_ERROR)
                        finish()
                    }
                }
            })
        }
    }

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
        btnCreate.setOnClickListener {
            goalCreateViewModel.createGoal(taskDate, taskName, taskValue)
        }
    }

    private fun initFocusListeners() {
        txtName.setOnFocusChangeListener { _, _ -> validateCreateButton() }
        txtValue.setOnFocusChangeListener { _, _ -> validateCreateButton() }
    }

    private fun initFields() {
        setData(calendarToday.toCurrentDateSystemString(DateFormat.SHORT))
        txtName.callFunctionAfterTextChanged { validateCreateButton() }
        txtValue.toCurrencyFormat {
            validateCreateButton()
        }
        tilValue.hint = resources.getString(R.string.goal_create_goal_value, MONEY_MIN.toCurrency())
    }

    // TODO
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

    private fun validateCreateButton() = goalCreateViewModel.validateFields(taskName, taskValue)

    private fun openCalendar() = startActivityForResult(
        Actions.openGoalCreateCalendar(this).putExtra(CALENDAR_TAG, calendarToday),
        REQUEST_CODE_CALENDAR
    )

    companion object {
        private const val MONEY_MIN = 1f
        private const val ACTIVITY_ERROR = -3
        private const val REQUEST_CODE_CALENDAR = 200
        private const val CALENDAR_TAG = "CALENDAR"
    }
}