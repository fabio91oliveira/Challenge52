package oliveira.fabio.challenge52.presentation.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.CalendarView
import features.goalcreate.R
import kotlinx.android.synthetic.main.activity_calendar.*
import oliveira.fabio.challenge52.BaseActivity
import java.util.*

class CalendarActivity : BaseActivity(), CalendarView.OnDateChangeListener {

    private val calendar by lazy { intent?.extras?.getSerializable(CALENDAR_TAG) as Calendar }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    override fun onSelectedDayChange(view: CalendarView, year: Int, month: Int, dayOfMonth: Int) {
        with(calendar) {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }
    }

    private fun init() {
        setupView()
        setupToolbar()
        initCalendar()
        initClickListener()
    }

    private fun setupView() = setContentView(R.layout.activity_calendar)

    private fun setupToolbar() {
        with(toolbar) {
            setSupportActionBar(this)
            supportActionBar?.title = resources.getString(R.string.goal_create_calendar_choose_date)
            setNavigationOnClickListener { finish() }
        }
    }

    private fun initCalendar() {
        with(cldCalendar) {
            minDate = Calendar.getInstance().timeInMillis
            date = calendar.timeInMillis
            setOnDateChangeListener(this@CalendarActivity)
        }
    }

    private fun initClickListener() {
        btnSelect.setOnClickListener {
            Intent().apply {
                putExtra(CALENDAR_TAG, calendar)
                setResult(Activity.RESULT_OK, this)
                finish()
            }
        }
    }

    companion object {
        private const val CALENDAR_TAG = "CALENDAR"
    }
}