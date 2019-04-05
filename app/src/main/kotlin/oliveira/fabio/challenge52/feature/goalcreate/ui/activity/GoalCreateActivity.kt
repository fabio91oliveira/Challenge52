package oliveira.fabio.challenge52.feature.goalcreate.ui.activity

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_goal_create.*
import oliveira.fabio.challenge52.R
import oliveira.fabio.challenge52.feature.goalcreate.viewmodel.GoalCreateViewModel
import oliveira.fabio.challenge52.model.entity.Goal
import oliveira.fabio.challenge52.util.extension.callFunctionAfterTextChanged
import oliveira.fabio.challenge52.util.extension.toCurrencyFormat
import oliveira.fabio.challenge52.util.extension.toCurrentFormat
import oliveira.fabio.challenge52.util.extension.toDate
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class GoalCreateActivity : AppCompatActivity() {

    private val goalCreateViewModel: GoalCreateViewModel by viewModel()
    private val calendar by lazy { Calendar.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_create)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CALENDAR) {
            val calendar = data?.getSerializableExtra(CALENDAR_TAG) as Calendar
            this.calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR))
            this.calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH))
            this.calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))

            setData(this.calendar.toCurrentFormat(resources.getString(R.string.date_pattern)))
            content.requestFocus()
        }
    }

    private fun init() {
        setupToolbar()
        initFields()
        initLiveData()
        initClickListeners()
        initFocusListeners()
        initAnimations()
    }

    private fun initLiveData() {
        goalCreateViewModel.mutableLiveData.observe(this, Observer {
            when (it) {
                true -> {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                false -> {
                }
            }
        })
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = resources.getString(R.string.goal_create_new_goal)
        toolbar.setNavigationOnClickListener { finish() }
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
        setData(calendar.toCurrentFormat(resources.getString(R.string.date_pattern)))
        txtName.callFunctionAfterTextChanged { validateCreateButton() }
        txtValue.toCurrencyFormat {
            validateCreateButton()
        }
    }

    private fun initAnimations() {
//        val fadeIn = AlphaAnimation(0f, 1f)
//        fadeIn.interpolator = DecelerateInterpolator()
//        fadeIn.duration = 900
//
//        val animation = AnimationSet(false)
//        animation.addAnimation(fadeIn)
//        tilName.animation = animation
//        tilValue.animation = animation
//        tilData.animation = animation
//        btnCreate.animation = animation

        val valueAnimator = ValueAnimator.ofFloat(-300f, 0f)
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.duration = 500
        valueAnimator.addUpdateListener {
            val progress = it.animatedValue as Float
            card.translationY = progress
        }
        valueAnimator.start()

        val valueAnimator2 = ValueAnimator.ofFloat(-300f, 0f)
        valueAnimator2.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator2.duration = 500
        valueAnimator2.addUpdateListener {
            val progress = it.animatedValue as Float
            btnCreate.translationX = progress
        }
        valueAnimator2.start()
    }

    private fun openCalendar() = Intent(this@GoalCreateActivity, CalendarActivity::class.java).apply {
        putExtra(CALENDAR_TAG, calendar)
        startActivityForResult(this, REQUEST_CODE_CALENDAR)
    }

    private fun validateCreateButton() {
        btnCreate.isEnabled = isAllFieldsFilled()
    }

    private fun isAllFieldsFilled() =
        txtName.text.toString().isNotEmpty() && (!goalCreateViewModel.isZero(removeMoneyMask(txtValue.text.toString())))

    private fun getFilledGoal() = Goal().apply {
        initialDate = txtDate.toDate()
        name = txtName.text.toString()
        totalValue = removeMoneyMask(txtValue.text.toString()).toFloat() * TOTAL_WEEKS
    }

    private fun removeMoneyMask(value: String) = Regex(resources.getString(R.string.currency_mask)).replace(value, "")

    companion object {
        private const val REQUEST_CODE_CALENDAR = 200
        private const val CALENDAR_TAG = "CALENDAR"
        private const val TOTAL_WEEKS = 52
    }
}