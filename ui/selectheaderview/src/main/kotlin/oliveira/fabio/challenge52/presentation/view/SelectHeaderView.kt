package oliveira.fabio.challenge52.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.layout_select_header_view.view.*
import ui.selectheaderview.R

class SelectHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = DEFAULT_STYLE_RES
) : ConstraintLayout(context, attrs, defStyle) {

    private var clickButtonsListener: ClickButtonsListener? = null

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

    fun setClickButtonsListener(clickButtonsListener: ClickButtonsListener) {
        this.clickButtonsListener = clickButtonsListener
    }

    fun setTitleText(text: String) {
        txtItem.text = text
        //currentDate.time.getDateStringByFormat(DATE_PATTERN)
    }

    private fun setupClickListeners() {
        imgArrowLeft.setOnClickListener {
            clickButtonsListener?.onClickLeftListener()
        }

        imgArrowRight.setOnClickListener {
            clickButtonsListener?.onClickRightListener()
        }
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

                //
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
                //

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
    }

    interface ClickButtonsListener {
        fun onClickLeftListener()
        fun onClickRightListener()
    }
}