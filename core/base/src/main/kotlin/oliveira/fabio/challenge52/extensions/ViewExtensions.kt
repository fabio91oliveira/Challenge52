package oliveira.fabio.challenge52.extensions

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.text.NumberFormat
import java.util.*

fun EditText.toCurrencyAndTextChangeAction(func: (() -> Unit?)? = null) {
    var current = ""
    val maxLength = 22
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {

            if (current != s.toString() && s.toString().length < maxLength) {
                removeTextChangedListener(this)

                val parsedValue = s
                    .toString()
                    .onlyNumbers()
                    .toDoubleOrNull()
                    ?.div(100) ?: 0.0

                val stringFormatted = parsedValue.toStringMoney(
                    removeWhiteSpaces = true,
                    useCurrency = true
                )

                current = stringFormatted

                setText(stringFormatted)
                setSelection(text.length)

                func?.invoke()
                addTextChangedListener(this)
            } else {
                setText(current.onlyNumbers())
            }
        }
    })
}

fun View.doPopAnimation(duration: Long, func: () -> Unit) {
    animate()
        .apply {
            this.duration = duration
            scaleX(1.04f)
            scaleY(1.04f)
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

fun Button.setRipple(
    pressedColor: Int
) {
    val color = ContextCompat.getColor(this.context, pressedColor)
    background =
        RippleDrawable(ColorStateList(arrayOf(intArrayOf()), intArrayOf(color)), background, null)
}

fun View.doSlideDownAnimation() {
    val fadeIn = AlphaAnimation(0f, 1f)
    fadeIn.interpolator = DecelerateInterpolator()
    fadeIn.duration = 2000

    val anim = AnimationSet(false)
    anim.addAnimation(fadeIn)
    animation = anim

    val valueAnimator = ValueAnimator.ofFloat(-100f, 0f)
    valueAnimator.interpolator = AccelerateDecelerateInterpolator()
    valueAnimator.duration = 1000
    valueAnimator.addUpdateListener {
        val progress = it.animatedValue as Float
        translationY = progress
    }
    valueAnimator.start()
}

inline var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }