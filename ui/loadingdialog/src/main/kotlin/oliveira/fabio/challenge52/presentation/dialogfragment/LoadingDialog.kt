package oliveira.fabio.challenge52.presentation.dialogfragment

import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.dialog_fragment_loading_dialog.*
import ui.loadingdialog.R

class LoadingDialog(
    context: Context?,
    @StringRes private val resTitle: Int,
    @StringRes private val resDescription: Int
) : AlertDialog(context ?: throw IllegalArgumentException()) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_fragment_loading_dialog)
        setupTexts()
    }

    private fun setupTexts() {
        txtTitle.setText(resTitle)
        txtDescription.setText(resDescription)
    }
}