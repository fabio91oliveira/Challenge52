package oliveira.fabio.challenge52.presentation.bottomsheetdialogfragment

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.bottom_sheet_dialog_fragment_options_popup.*
import kotlinx.android.synthetic.main.item_option.view.*
import ui.optionsbottompopup.R

class OptionsBottomPopup : BottomSheetDialogFragment() {

    private val resTitle: Int by lazy {
        arguments?.getInt(
            TITLE
        ) ?: 0
    }

    private val options: Array<Option> by lazy {
        arguments?.getParcelableArray(
            OPTIONS
        ) as Array<Option>
    }

    private val finishWhenClickOption: Boolean by lazy {
        arguments?.getBoolean(FINISH_WHEN_CLICK_OPTION) ?: false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(
            R.layout.bottom_sheet_dialog_fragment_options_popup,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.also { dismiss() }
        init()
    }

    private fun init() {
        setupTitle()
        setupOptions()
    }

    private fun setupTitle() {
        if (resTitle != 0) {
            txtTitle.visibility = View.VISIBLE
            txtTitle.text = context?.resources?.getString(resTitle)
        }
    }

    private fun setupOptions() {
        options.forEach { option ->
            val view =
                LayoutInflater.from(context).inflate(R.layout.item_option, contentItems, false)
            view.imgIco.setImageResource(option.icon)
            view.txtOption.setText(option.text)
            view.setOnClickListener {
                option.optionsBottomPopupOptionListener.onClickOption()
                if (finishWhenClickOption) dismiss()
            }
            contentItems.addView(view)
        }
    }

    class Builder {
        private val args: Bundle = Bundle()

        fun setTitle(
            @StringRes resTitle: Int,
            @ColorRes resColor: Int = android.R.color.black
        ) =
            apply {
                args.putInt(TITLE, resTitle)
                args.putInt(TITLE_COLOR, resTitle)
            }

        fun setOptions(options: Array<Option>) =
            apply {
                args.putParcelableArray(OPTIONS, options)
            }

        fun setFinishWhenClickOption(hasToFinish: Boolean) = apply {
            args.putBoolean(FINISH_WHEN_CLICK_OPTION, hasToFinish)
        }

        fun build() = OptionsBottomPopup().apply {
            this.arguments = args
        }
    }

    @Parcelize
    data class Option(
        @DrawableRes val icon: Int,
        @StringRes val text: Int,
        val optionsBottomPopupOptionListener: OptionsBottomPopupOptionListener,
        @ColorRes val resColor: Int = android.R.color.black
    ) : Parcelable {
        interface OptionsBottomPopupOptionListener : Parcelable {
            fun onClickOption()
            override fun describeContents(): Int = 0
            override fun writeToParcel(dest: Parcel, flags: Int) {
            }
        }
    }

    companion object {
        const val TAG = "OptionsBottomPopupTag"
        private const val TITLE = "title"
        private const val TITLE_COLOR = "title_color"
        private const val OPTIONS = "options"
        private const val FINISH_WHEN_CLICK_OPTION = "finish_when_click_option"
    }
}