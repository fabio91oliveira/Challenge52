package oliveira.fabio.challenge52.extensions

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import java.text.DateFormat
import java.text.NumberFormat
import java.util.*


fun EditText.toCurrencyFormat(func: (() -> Unit?)? = null) {
    var current = ""
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            if (s.toString() != current) {
                removeTextChangedListener(this)

                val defaultCurrencySymbol = Currency.getInstance(Locale.getDefault()).symbol
                val regexPattern = "[$defaultCurrencySymbol,.]"

                val regex = Regex(regexPattern)
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

fun EditText.toDate(dateFormat: Int): Date {
    val sdf = DateFormat.getDateInstance(dateFormat)
    sdf.isLenient = false
    return sdf.parse(text.toString())
}

fun View.doPopAnimation(duration: Long, func: () -> Unit) {
    animate()
        .apply {
            this.duration = duration
            scaleX(1.1f)
            scaleY(1.1f)
            withEndAction {
                this.duration = duration
                scaleX(1f)
                scaleY(1f)
                withEndAction {
                    func.invoke()
                }
            }
            start()
        }

}