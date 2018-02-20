package `in`.jatindhankhar.wlosh.utils

import `in`.jatindhankhar.wlosh.R
import android.Manifest
import android.app.Activity
import android.view.View
import android.view.animation.*
import java.util.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.animation.AccelerateDecelerateInterpolator
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AlphaAnimation
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import java.io.File
import kotlin.reflect.KFunction0


/**
 * Created by jatin on 2/7/18.
 */
object Essentials {
     fun setFadeInAnimation(targetView : View, duration: Long = 200L)
    {
        val animation = AlphaAnimation(0.0f,1.0f)
        animation.duration = duration
        targetView.startAnimation(animation)

    }


    fun setStatusBarColor( ctx:Context,color: Int)
    {

        val window = (ctx as AppCompatActivity).window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.statusBarColor = ContextCompat.getColor(ctx, color);
            }
        }
    }

    fun setScaleAnimation(targetView: View,duration: Long = 200L,random: Boolean = true)
    {
        val anim = ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        anim.interpolator = AccelerateDecelerateInterpolator()

        if(random)
            anim.duration = Random().nextInt(501).toLong()
        else
            anim.duration = duration
        targetView.startAnimation(anim)
    }

    fun setslideUpAnimation(targetView: View,duration: Long = 200L)
    {
        this.setFadeInAnimation(targetView)
        targetView.animate()
                .translationYBy(120F)
                .translationY(0F).duration = duration
    }

    fun setslideFromLeftAnimation(targetView: View,duration: Long = 200L)
    {
        targetView.animate()
                .setInterpolator(DecelerateInterpolator(3.1f))
                .translationXBy(-100F)
                .translationX(0F).duration = 1000L
    }

    fun setSlideUpandLeftAnimation(tView: View,tDuration: Long = 200L)
    {
        this.setslideUpAnimation(targetView = tView,duration = tDuration)
        this.setslideFromLeftAnimation(targetView = tView,duration = tDuration)
    }

    fun getToggleIcon(gridCount :Int):Int
    {
        return if(gridCount == 2)
            R.drawable.toggle
        else
            R.drawable.view_list
    }

    fun checkIfHasSdWritePermission(context: Context):Boolean {
        if (Build.VERSION.SDK_INT >= 23) {

            return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

        }
        return true

    }

    fun checkandAskforStorageWritePermission(activity: Activity,onSuccess: KFunction0<Unit>)
    {
        if(checkIfHasSdWritePermission(activity.applicationContext))
        {
            onSuccess()
        }
        else
        {
            askforStorageWritePermission(activity,onSuccess)
        }
    }

     fun askforStorageWritePermission(activity: Activity, onSuccess: KFunction0<Unit>)
    {
        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object: PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        onSuccess()
                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                        token?.continuePermissionRequest()
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                        response?.let {
                            if(it.isPermanentlyDenied)
                            {
                                showSettingsDialog(activity)
                            }

                        }
                    }

                })
                .check()
    }

    private fun showSettingsDialog(activity: Activity) {
        activity.alert("This app needs permission to use this feature. Grant them in the settings","Needs Permissions")
        {
            positiveButton("Settings") {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package",activity.packageName,null)
                activity.startActivityForResult(intent,101)

            }
            noButton {  }
        }.show()
    }


}