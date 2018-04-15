package `in`.jatindhankhar.wlosh.ui

import `in`.jatindhankhar.wlosh.R
import `in`.jatindhankhar.wlosh.utils.Constants
import `in`.jatindhankhar.wlosh.utils.Essentials
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatCheckBox
import kotlinx.android.synthetic.main.activity_key_prompt.*
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class KeyPromptActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_key_prompt)

        checkBox.setOnClickListener {
            if ((it as AppCompatCheckBox).isChecked) {
                if (accessKey.text.isEmpty()) {
                    accessKeyInput.error = "Cannot be empty "
                    it.isChecked = false
                } else {
                    Essentials.prefWrite(applicationContext, Constants.ACCESS_KEY_PREF, accessKey.text.toString())
                    toast("Saving key ${accessKey.text}")
                    startActivity(intentFor<MainActivity>().clearTop())
                    finish()
                }
            }
        }
    }
}
