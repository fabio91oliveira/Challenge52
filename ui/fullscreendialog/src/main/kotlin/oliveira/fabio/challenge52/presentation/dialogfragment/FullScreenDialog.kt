package oliveira.fabio.challenge52.presentation.dialogfragment

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_fragment_fullscreen_dialog.*
import ui.fullscreendialog.R

class FullScreenDialog : DialogFragment() {

    private val resTitle: Int by lazy {
        arguments?.getInt(
            TITLE
        ) ?: 0
    }

    private val resSubtitle: Int by lazy {
        arguments?.getInt(
            SUBTITLE
        ) ?: 0
    }

    private val resConfirmText: Int by lazy {
        arguments?.getInt(
            CONFIRM_TEXT
        ) ?: 0
    }

    private val resCancelText: Int by lazy {
        arguments?.getInt(
            CANCEL_TEXT
        ) ?: 0
    }

    private val backgroundColor: BackgroundColor by lazy {
        arguments?.getSerializable(BACKGROUND_COLOR)?.let {
            it as BackgroundColor
        } ?: BackgroundColor.DEFAULT
    }

    private val closeListener: FullScreenDialogCloseListener? by lazy {
        arguments?.getParcelable(
            CLOSE_LISTENER
        ) as FullScreenDialogCloseListener?
    }

    private val confirmListener: FullScreenDialogConfirmListener? by lazy {
        arguments?.getParcelable(
            CONFIRM_LISTENER
        ) as FullScreenDialogConfirmListener?
    }

    private val cancelListener: FullScreenDialogCancelListener? by lazy {
        arguments?.getParcelable(
            CANCEL_LISTENER
        ) as FullScreenDialogCancelListener?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(
            R.layout.dialog_fragment_fullscreen_dialog,
            container,
            false
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.also { dismiss() }
        init()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.apply {
            attributes.windowAnimations = R.style.FullScreenDialogTheme
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.apply {
            setCancelable(false)
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    private fun init() {
        setupColor()
        setupTitle()
        setupSubtitle()
        setupCloseListener()
        setupConfirmButton()
        setupCancelButton()
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

    private fun setupColor() {
        dialog?.window?.setBackgroundDrawableResource(backgroundColor.getColor())
    }

    private fun setupTitle() {
        check(resTitle != 0) { "Must have title set." }
        txtTitle.text = context?.resources?.getString(resTitle)
    }

    private fun setupSubtitle() {
        check(resSubtitle != 0) { "Must have subtitle set." }
        txtSubtitle.text = context?.resources?.getString(resSubtitle)
    }

    private fun setupCloseListener() {
        imgClose.setOnClickListener {
            closeListener?.onClickCloseButton()
            dismiss()
        }
    }

    private fun setupConfirmButton() {
        confirmListener?.also { confirm ->
            btnConfirm.text = context?.resources?.getString(resConfirmText)
            btnConfirm.visibility = View.VISIBLE
            btnConfirm.setOnClickListener {
                confirm.onClickConfirmButton()
                dismiss()
            }
        }
        setupConfirmButtonColor()
    }

    private fun setupCancelButton() {
        cancelListener?.also { cancel ->
            btnCancel.text = context?.resources?.getString(resCancelText)
            btnCancel.visibility = View.VISIBLE
            btnCancel.setOnClickListener {
                cancel.onClickCancelButton()
                dismiss()
            }
        }
    }

    private fun setupConfirmButtonColor() {
        context?.also {
            val color = ContextCompat.getColor(it, backgroundColor.getColor())
            with(btnConfirm) {
                background = getBackgroundDrawable(
                    color,
                    background
                )
                setTextColor(color)
            }
        }
    }

    class Builder {
        private val args: Bundle = Bundle()

        fun setBackgroundColor(background: BackgroundColor) = apply {
            args.putSerializable(BACKGROUND_COLOR, background)
        }

        fun setTitle(@StringRes resTitle: Int) = apply {
            args.putInt(TITLE, resTitle)
        }

        fun setSubtitle(@StringRes resSubtitle: Int) = apply {
            args.putInt(SUBTITLE, resSubtitle)
        }

        fun setCloseAction(listener: FullScreenDialogCloseListener) = apply {
            args.putParcelable(CLOSE_LISTENER, listener)
        }

        fun setupConfirmButton(
            @StringRes resConfirm: Int, listener: FullScreenDialogConfirmListener
        ) =
            apply {
                args.putInt(CONFIRM_TEXT, resConfirm)
                args.putParcelable(CONFIRM_LISTENER, listener)
            }

        fun setupCancelButton(@StringRes resCancel: Int, listener: FullScreenDialogCancelListener) =
            apply {
                args.putInt(CANCEL_TEXT, resCancel)
                args.putParcelable(CANCEL_LISTENER, listener)
            }

        fun build() = FullScreenDialog().apply {
            this.arguments = args
        }
    }

    interface FullScreenDialogCloseListener : Parcelable {
        fun onClickCloseButton()
        override fun describeContents(): Int = 0
        override fun writeToParcel(dest: Parcel, flags: Int) {
        }
    }

    interface FullScreenDialogConfirmListener : Parcelable {
        fun onClickConfirmButton()
        override fun describeContents(): Int = 0
        override fun writeToParcel(dest: Parcel, flags: Int) {
        }
    }

    interface FullScreenDialogCancelListener : Parcelable {
        fun onClickCancelButton()
        override fun describeContents(): Int = 0
        override fun writeToParcel(dest: Parcel, flags: Int) {
        }
    }

    enum class BackgroundColor(@ColorRes private val resColor: Int) {
        DEFAULT(R.color.fullscreen_dialog_color_default),
        GREEN(R.color.fullscreen_dialog_color_green),
        YELLOW(R.color.fullscreen_dialog_color_yellow),
        RED(R.color.fullscreen_dialog_color_red),
        BLUE(R.color.fullscreen_dialog_color_blue),
        BLACK(android.R.color.black);

        fun getColor() = resColor
    }

    companion object {
        const val TAG = "FullScreenDialog"
        const val TITLE = "TITLE"
        const val SUBTITLE = "SUBTITLE"
        const val BACKGROUND_COLOR = "BACKGROUND_COLOR"
        const val CLOSE_LISTENER = "CLOSE_LISTENER"
        const val CONFIRM_TEXT = "CONFIRM_TEXT"
        const val CONFIRM_LISTENER = "CONFIRM_LISTENER"
        const val CANCEL_LISTENER = "CANCEL_LISTENER"
        const val CANCEL_TEXT = "CANCEL_TEXT"
    }
}