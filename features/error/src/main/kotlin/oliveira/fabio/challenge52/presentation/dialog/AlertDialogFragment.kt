package oliveira.fabio.challenge52.presentation.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import features.error.R
import kotlinx.android.synthetic.main.fragment_alert_dialog.*

class AlertDialogFragment : DialogFragment() {

    private val resImage: Int by lazy {
        arguments?.getInt(
            RES_IMAGE
        ) ?: 0
    }

    private val resTitle: Int by lazy {
        arguments?.getInt(
            TITLE
        ) ?: 0
    }

    private val subtitle: Int by lazy {
        arguments?.getInt(
            SUBTITLE
        ) ?: 0
    }

    private var block: (() -> Unit?)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_alert_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isCancelable = false
        }
        init()
    }

    override fun onStart() {
        super.onStart()
        dialog?.apply {
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    private fun init() {
        setupImage()
        setupTexts()
        setupDefaultClickListeners()
    }

    private fun setupImage() {
        if (resImage != 0) imgTop.setImageResource(resImage)
    }

    private fun setupTexts() {
        if (resTitle != 0) txtTitle.text = context?.resources?.getString(resTitle)
        if (subtitle != 0) txtSubtitle.text = context?.resources?.getString(subtitle)
    }

    private fun setupDefaultClickListeners() {
        btnOk.setOnClickListener {
            dismiss()
            block?.invoke()
        }
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val view = View.inflate(context, R.layout.fragment_alert_dialog, null)
//
//        val builder = AlertDialog.Builder(view.context)
//        builder.setView(view)
//
//        val dialogBuilder = builder.create()
//        dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//
//        init(view)
//        return dialogBuilder
//    }

    companion object {
        const val TAG = "AlertDialogFragment"
        const val RES_IMAGE = "RES_IMAGE"
        const val TITLE = "TITLE"
        const val SUBTITLE = "SUBTITLE"

        fun newInstance(resImage: Int, title: Int, subTitle: Int, block: (() -> Unit)? = null) =
            AlertDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(RES_IMAGE, resImage)
                    putInt(TITLE, title)
                    putInt(SUBTITLE, subTitle)
                }
                block?.also {
                    this.block = it
                }
            }
    }
}