package oppen.smolicon

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class InfoDialog(context: Context) {

    private val dialog: AlertDialog

    init {
        val view = View.inflate(context, R.layout.info, null)
        dialog = MaterialAlertDialogBuilder(context, R.style.Aldialog)
            .setView(view)
            .setPositiveButton(context.getString(R.string.close)){ _, _ ->

            }
            .create()

        val infoLabel = view.findViewById<AppCompatTextView>(R.id.info_label)
        infoLabel.append("Smolicon: 16x16 pixel drawing tool\n")
        val versionName = BuildConfig.VERSION_NAME
        infoLabel.append("$versionName\n")
        infoLabel.append("© 2021 ÖLAB/Öppenlab")

        val oppenDigitalButton = view.findViewById<AppCompatButton>(R.id.oppen_digital_web_button)
        oppenDigitalButton.setOnClickListener{
            context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://oppen.digital")
            })
        }

    }

    fun show(){
        dialog.show()
    }
}