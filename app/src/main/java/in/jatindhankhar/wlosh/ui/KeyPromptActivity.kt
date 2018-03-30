package `in`.jatindhankhar.wlosh.ui

import `in`.jatindhankhar.wlosh.R
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.AppCompatCheckBox
import android.widget.CheckBox
import kotlinx.android.synthetic.main.activity_key_prompt.*
import org.jetbrains.anko.startActivity

class KeyPromptActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_key_prompt)

        checkBox.setOnClickListener {
            if((it as AppCompatCheckBox).isChecked)
            {
                if(accessKey.text.isEmpty()) {
                    accessKeyInput.error = "Cannot be empty "
                    it.isChecked = false;
                }
                else
                {
                    startActivity<MainActivity>()}
            }
        }
    }
}
