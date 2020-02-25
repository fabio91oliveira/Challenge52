package oliveira.fabio.challenge52.presentation.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.layout_state_view.view.*
import ui.stateview.R

class StateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = DEFAULT_STYLE_RES
) : ConstraintLayout(context, attrs, defStyle) {

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_state_view, this, true)

        attrs?.let {
            context.theme.obtainStyledAttributes(
                attrs, R.styleable.StateView, DEFAULT_STYLE_ATTR, DEFAULT_STYLE_RES
            )
        }?.also { attributes ->
            setImage(
                attributes.getResourceId(
                    R.styleable.StateView_stateViewImage,
                    DEFAULT_IMAGE_ATTR
                )
            )
            setTitle(attributes.getString(R.styleable.StateView_stateViewTitle).orEmpty())
            setDescription(attributes.getString(R.styleable.StateView_stateViewDescription).orEmpty())
            attributes.recycle()
        }
    }

    fun setTitle(title: String) {
        txtTitle.text = title
        txtTitle.visibility = View.VISIBLE
    }

    fun setDescription(description: String) {
        txtDescription.text = description
        txtDescription.visibility = View.VISIBLE
    }

    fun setImage(@DrawableRes image: Int) {
        imgTop.setImageResource(image)
        imgTop.visibility = View.VISIBLE
    }

    fun setupButton(
        buttonText: String?,
        block: () -> Unit
    ) {
        btnAction.text = buttonText.orEmpty()
        btnAction.visibility = View.VISIBLE
        btnAction.setOnClickListener {
            block()
        }
        setupButtonRipple()
    }

    private fun setupButtonRipple() {
        context?.also {
            val color = ContextCompat.getColor(it, android.R.color.white)
            with(btnAction) {
                background = getBackgroundDrawable(
                    color,
                    background
                )
            }
        }
    }

    private fun getBackgroundDrawable(
        pressedColor: Int,
        backgroundDrawable: Drawable
    ): RippleDrawable {
        return RippleDrawable(getPressedState(pressedColor), backgroundDrawable, null)
    }

    private fun getPressedState(pressedColor: Int): ColorStateList {
        return ColorStateList(arrayOf(intArrayOf()), intArrayOf(pressedColor))
    }

    private companion object {
        private const val DEFAULT_STYLE_ATTR = 0
        private const val DEFAULT_STYLE_RES = 0
        private const val DEFAULT_IMAGE_ATTR = 0
    }
}