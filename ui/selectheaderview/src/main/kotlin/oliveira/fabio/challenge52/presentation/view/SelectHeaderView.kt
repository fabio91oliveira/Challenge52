package oliveira.fabio.challenge52.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.layout_select_header_view.view.*
import oliveira.fabio.challenge52.extensions.getDateStringByFormat
import ui.selectheaderview.R
import java.util.*

class SelectHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = DEFAULT_STYLE_RES
) : ConstraintLayout(context, attrs, defStyle) {

    private var currentDate = Calendar.getInstance()

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_select_header_view, this, true)

        attrs?.let {
            context.theme.obtainStyledAttributes(
                attrs, R.styleable.SelectHeaderView, DEFAULT_STYLE_ATTR, DEFAULT_STYLE_RES
            )
        }?.also { attributes ->
            setBackground(
                attributes.getResourceId(
                    R.styleable.SelectHeaderView_backgroundColor,
                    0
                )
            )
            setComponentsColor(
                attributes.getResourceId(
                    R.styleable.SelectHeaderView_componentsColor,
                    0
                )
            )
            setTextColor(
                attributes.getResourceId(
                    R.styleable.SelectHeaderView_textColor,
                    0
                )
            )
            attributes.recycle()
        }

        refreshDate()
        setupClickListeners()
    }

    fun setBackground(@ColorRes color: Int) {
        if (color != 0) content.setBackgroundColor(ContextCompat.getColor(context, color))
    }

    fun setComponentsColor(@ColorRes color: Int) {
        if (color != 0) {
            imgArrowLeft.setColorFilter(ContextCompat.getColor(context, color))
            imgArrowRight.setColorFilter(ContextCompat.getColor(context, color))
        }
    }

    fun setTextColor(@ColorRes color: Int) {
        if (color != 0) txtItem.setTextColor(ContextCompat.getColor(context, color))
    }

    private fun refreshDate() {
        txtItem.text = currentDate.time.getDateStringByFormat(DATE_PATTERN)
    }

    private fun setupClickListeners() {
        imgArrowLeft.setOnClickListener {
            previousDate()
            animateTextToLeft()
        }

        imgArrowRight.setOnClickListener {
            nextDate()
            animateTextToRight()
        }
    }

    private fun previousDate() {
        currentDate.add(Calendar.MONTH, -1)
    }

    private fun nextDate() {
        currentDate.add(Calendar.MONTH, 1)
    }

    private fun animateTextToLeft() {
        txtItem.animate().apply {
            duration = 75
            translationX(-200f)
            alpha(0f)
            withEndAction {
                duration = 0
                translationX(0f)
                translationY(-100f)
                refreshDate()
                withEndAction {
                    duration = 50
                    translationY(0f)
                    alpha(1f)
                }
            }
        }
    }

    private fun animateTextToRight() {
        txtItem.animate().apply {
            duration = 75
            translationX(200f)
            alpha(0f)
            withEndAction {
                duration = 0
                translationX(0f)
                translationY(-100f)
                refreshDate()
                withEndAction {
                    duration = 50
                    translationY(0f)
                    alpha(1f)
                }
            }
        }
    }

    private companion object {
        private const val DEFAULT_STYLE_ATTR = 0
        private const val DEFAULT_STYLE_RES = 0
        private const val DATE_PATTERN = "MMMM/yyyy"
    }
}