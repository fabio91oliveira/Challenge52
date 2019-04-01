package oliveira.fabio.challenge52.feature.targetcreate.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_calendar.*
import oliveira.fabio.challenge52.R
import java.util.*

class CalendarActivity : AppCompatActivity(), CalendarView.OnDateChangeListener {

    private val calendar by lazy { intent?.extras?.getSerializable(CALENDAR_TAG) as Calendar }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        init()
    }

    override fun onSelectedDayChange(view: CalendarView, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
    }

    private fun init() {
        setupToolbar()
        initCalendar()
        initClickListener()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = resources.getString(R.string.target_create_calendar_choose_date)
        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun initCalendar() {
        cldCalendar.minDate = Calendar.getInstance().timeInMillis
        cldCalendar.date = calendar.timeInMillis
        cldCalendar.setOnDateChangeListener(this)
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