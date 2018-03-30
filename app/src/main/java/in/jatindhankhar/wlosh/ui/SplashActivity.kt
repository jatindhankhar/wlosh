package `in`.jatindhankhar.wlosh.ui

import `in`.jatindhankhar.wlosh.utils.Constants
import `in`.jatindhankhar.wlosh.utils.Essentials
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // If Access Key stored, continue to MainActivity else ask for Key Again
        if(Essentials.prefExists(this,Constants.ACCESS_KEY_PREF))
           startActivity<MainActivity>()
        else
            startActivity<KeyPromptActivity>()
        finish()
    }
}
