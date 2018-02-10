package `in`.jatindhankhar.wlash.utils

import android.view.View
import android.view.animation.*
import java.time.Duration
import java.util.*
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.view.animation.AccelerateDecelerateInterpolator
import android.opengl.ETC1.getWidth
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat.animate
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.view.animation.AlphaAnimation






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
}