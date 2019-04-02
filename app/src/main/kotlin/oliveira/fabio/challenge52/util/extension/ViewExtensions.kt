package oliveira.fabio.challenge52.util.extension

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import oliveira.fabio.challenge52.R
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


fun EditText.toCurrencyFormat(func: (() -> Unit?)? = null) {
    var current = ""
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            if (s.toString() != current) {
                removeTextChangedListener(this)

                val regex = Regex(context.getString(R.string.currency_mask))
                val cleanString = regex.replace(s.toString(), "")

                val parsed = cleanString.toDouble()
                val formatted = NumberFormat.getCurrencyInstance().format((parsed / 100))

                current = formatted
                setText(formatted)
                setSelection(formatted.length)
                addTextChangedListener(this)
            }
            func?.invoke()
        }
    })
}

fun EditText.callFunctionAfterTextChanged(func: () -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) = func.invoke()
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    })
}

fun EditText.toDate(): Date {
    val sdf = SimpleDateFormat(context.resources.getString(R.string.date_pattern))
    return sdf.parse(text.toString())
}