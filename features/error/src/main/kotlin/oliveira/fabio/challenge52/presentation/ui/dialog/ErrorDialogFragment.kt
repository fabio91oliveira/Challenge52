package oliveira.fabio.challenge52.presentation.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import features.error.R
import kotlinx.android.synthetic.main.dialog_fragment_error.*

class ErrorDialogFragment : DialogFragment() {

    private val message: String by lazy {
        arguments?.getString(
            MESSAGE,
            resources.getString(R.string.goals_generic_error)
        ) as String
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_fragment_error, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.ErrorDialogFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog.window?.apply {
            attributes.windowAnimations = R.style.ErrorDialogFragment
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.apply {
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    private fun init() {
        setupMessage()
        setupClickListener()
    }

    private fun setupMessage() {
        txtMessage.text = message
    }

    private fun setupClickListener() {
        btnGoBack.setOnClickListener { dismiss() }
    }

    companion object {
        const val TAG = "ErrorDialogFragment"
        const val MESSAGE = "MESSAGE"
    }
}