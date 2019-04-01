package oliveira.fabio.challenge52.util.extension

import android.content.Context
import oliveira.fabio.challenge52.R
import java.text.SimpleDateFormat
import java.util.*

fun Calendar.toCurrentFormat(context: Context): String {
    val sdf = SimpleDateFormat(context.resources.getString(R.string.date_pattern))
    return sdf.format(time)
}