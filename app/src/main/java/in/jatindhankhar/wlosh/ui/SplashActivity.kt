package `in`.jatindhankhar.wlosh.ui

import `in`.jatindhankhar.wlosh.utils.Constants
import `in`.jatindhankhar.wlosh.utils.Essentials
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // If Access Key stored, continue to MainActivity else ask for Key Again
        if (Essentials.prefExists(this, Constants.ACCESS_KEY_PREF))
            startActivity(intentFor<MainActivity>().clearTop())
        else
            startActivity(intentFor<KeyPromptActivity>().clearTop())
        finish()

    }
}
