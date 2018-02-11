package `in`.jatindhankhar.wlosh.ui

import `in`.jatindhankhar.wlosh.R
import `in`.jatindhankhar.wlosh.model.Response
import `in`.jatindhankhar.wlosh.utils.Constants
import `in`.jatindhankhar.wlosh.utils.Essentials
import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.Snackbar
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.activity_images_detail.*
import kotlinx.android.synthetic.main.image_detail_bottom_sheet.*
import android.view.animation.LinearInterpolator




class ImagesDetailActivity : AppCompatActivity() {

    private lateinit var mTarget: Target
    private  var mBitmap: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images_detail)
        mTarget = object :Target
        {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            }

            override fun onBitmapFailed(errorDrawable: Drawable?) {
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {

                bitmap?.let { detail_image.setImageBitmap(it); mBitmap = it }
            }

        }


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val response = intent.getParcelableExtra<Response?>(Constants.INTENT_RESPONSE_KEY)
        Toast.makeText(this,response?.user?.name,Toast.LENGTH_LONG).show()
       val sheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        val picasso = Picasso.with(this)
        response?.urls?.regular?.let { picasso.load(it).into(mTarget) }
        response?.color?.let {
            bottom_sheet.setBackgroundColor(Color.parseColor(it))

            }
        bottom_sheet.background.alpha = 180
        

      //  val wallpaperManager = WallpaperManager.getInstance(this)
       // val wallpaper = (detail_image.buildDrawingCache())
        fab_save.setOnClickListener {

            animation(it)
            if(mBitmap != null)
            {
                it.isEnabled = false
                val myWallpaperManager = WallpaperManager.getInstance(applicationContext)
                try {
                    myWallpaperManager.setBitmap(mBitmap)
                    Snackbar.make(layout_container,"Wallpaper applied successfully",Snackbar.LENGTH_LONG).show()
                    it.clearAnimation()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this,"There was some error",Toast.LENGTH_LONG).show()
                    it.clearAnimation()
                }

            }

        }
        up_arrow.setOnClickListener {
            if (sheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED)
            {
                sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            else if(sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
            {
                sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
       sheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
           override fun onSlide(bottomSheet: View, slideOffset: Float) {
              //fab_save.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
           }

           override fun onStateChanged(bottomSheet: View, newState: Int) {
               when(newState)
               {
                   BottomSheetBehavior.STATE_HIDDEN -> {

                   }
                   BottomSheetBehavior.STATE_EXPANDED -> {

                       Essentials.setslideUpAnimation(up_arrow)
                       up_arrow.rotation = up_arrow.rotation - 180
                   }

                   BottomSheetBehavior.STATE_COLLAPSED -> {
                       up_arrow.rotation = up_arrow.rotation - 180
                   }

                   BottomSheetBehavior.STATE_DRAGGING -> {

                   }
                   BottomSheetBehavior.STATE_SETTLING -> {

                   }

                   else -> {

                   }
               }
           }

       })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun animation(view: View) {
        val anim = RotateAnimation(0f, (360 * 4).toFloat(),
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)

        anim.interpolator = LinearInterpolator()
        anim.duration = 500
        // anim.setRepeatMode(Animation.INFINITE);
        anim.repeatCount = Animation.INFINITE
        anim.repeatMode = Animation.RESTART
        view.startAnimation(anim)
    }
}
