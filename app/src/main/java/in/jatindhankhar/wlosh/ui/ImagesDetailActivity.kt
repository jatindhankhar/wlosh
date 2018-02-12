package `in`.jatindhankhar.wlosh.ui

import `in`.jatindhankhar.wlosh.R
import `in`.jatindhankhar.wlosh.model.Response
import `in`.jatindhankhar.wlosh.utils.Constants
import `in`.jatindhankhar.wlosh.utils.Essentials
import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.View
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.activity_images_detail.*
import kotlinx.android.synthetic.main.image_detail_bottom_sheet.*
import kotlinx.android.synthetic.main.uploader_info_view.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread


class ImagesDetailActivity : AppCompatActivity() {

    private lateinit var mTarget: Target
    private lateinit var mWallpaperManager: WallpaperManager
    private var mBitmap: Bitmap? = null
    private var mFullScreen = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images_detail)
        // Set FullScreen
        toggleFullscreen(mFullScreen)

        mWallpaperManager = WallpaperManager.getInstance(this)
        mTarget = object : Target {

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            }

            override fun onBitmapFailed(errorDrawable: Drawable?) {

            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {

                bitmap?.let { detail_image.setImageBitmap(it); mBitmap = it;
                 imageProgress.visibility = View.GONE
                  detail_image.visibility = View.VISIBLE
                }
            }

        }

        detail_image?.setOnClickListener {
            // Flip and set
            toggleFullscreen(!mFullScreen)
            mFullScreen = !mFullScreen
        }


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val response = intent.getParcelableExtra<Response?>(Constants.INTENT_RESPONSE_KEY)
        val sheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        val picasso = Picasso.with(this)
        response?.urls?.regular?.let { picasso.load(it).into(mTarget) }
        response?.color?.let {
            bottom_sheet.setBackgroundColor(Color.parseColor(it))

        }
        val outValue = TypedValue()
        this.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        up_arrow.setBackgroundResource(outValue.resourceId);
        response?.user?.name.let { user_name.text = it }
        response?.user?.profileImage?.medium.let { picasso.load(it).into(user_profile); }
        bottom_sheet.background.alpha = 180
        val progressBarcolor = ContextCompat.getColor(this, R.color.colorPrimary)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            val wrapDrawable = DrawableCompat.wrap(fabProgress.indeterminateDrawable);
            DrawableCompat.setTint(wrapDrawable, progressBarcolor);
            fabProgress.indeterminateDrawable = DrawableCompat.unwrap(wrapDrawable);
        } else {
            fabProgress.indeterminateDrawable.setColorFilter(progressBarcolor, PorterDuff.Mode.SRC_IN);
        }


        fab_save.setOnClickListener {
            it.isEnabled = false
            fabProgress.visibility = View.VISIBLE
            doAsync {
                val result = setUserWallpaper(mBitmap)
                uiThread {
                    fabProgress.visibility = View.INVISIBLE
                    if (result) {
                        snackbar(layout_container, "Wallpaper Set Successful")
                        fab_save.setImageResource(R.drawable.done)
                    } else {
                        fab_save.setImageResource(R.drawable.error_outline)
                        toast("Cannot set Wallpaper !")
                    }
                }
            }

        }


        up_arrow.setOnClickListener {
            if (sheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
        sheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                //fab_save.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
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

    private fun setUserWallpaper(bitmap: Bitmap?): Boolean {
        bitmap?.let {

            return try {
                mWallpaperManager.setBitmap(mBitmap)
                true
            } catch (e: Exception) {
                false
            }
        }
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun toggleFullscreen(fullscreen: Boolean) {

        //
        if(fullscreen){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val mDecorView = window.decorView
            mDecorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar

                    or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar

                    or View.SYSTEM_UI_FLAG_IMMERSIVE)
        }

        else
        {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }
        }
        else
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            {
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            }
            else
            window.decorView.systemUiVisibility = 0
        }

        }}

