package `in`.jatindhankhar.wlosh.ui

import `in`.jatindhankhar.wlosh.R
import `in`.jatindhankhar.wlosh.model.Response
import `in`.jatindhankhar.wlosh.network.UnSplashClient
import `in`.jatindhankhar.wlosh.utils.Constants
import `in`.jatindhankhar.wlosh.utils.Essentials
import `in`.jatindhankhar.wlosh.utils.Essentials.checkandAskforStorageWritePermission
import android.Manifest
import android.app.WallpaperManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.TypedValue
import android.view.View
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.activity_images_detail.*
import kotlinx.android.synthetic.main.image_detail_bottom_sheet.*
import kotlinx.android.synthetic.main.image_info_view.*
import kotlinx.android.synthetic.main.uploader_info_view.*
import org.jetbrains.anko.*
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.design.snackbar
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.reflect.KFunction0


class ImagesDetailActivity : AppCompatActivity() {

    private lateinit var mTarget: Target
    private lateinit var mWallpaperManager: WallpaperManager
    private  lateinit var mUnSplashClient: UnSplashClient
    private val TAG = ImagesDetailActivity::class.java.simpleName
    private var mBitmap: Bitmap? = null
    private var mFullScreen = false
    private var mHtmlLink:String? = null
    private var mImageId:String? = null
    private var mImageName:String? = "image.jpg"
    private var mAuthorName:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images_detail)
        mUnSplashClient = initUnSplashClient()

        // Set FullScreen
        toggleFullscreen(mFullScreen)
        fab_save.isEnabled = false
        save_image_button.setOnClickListener{ onSaveButtonClicked()}
        more_info_button.setOnClickListener { onMoreButtonClicked() }
        share_image_button.setOnClickListener { onShareButtonClicked() }
        mWallpaperManager = WallpaperManager.getInstance(this)
        mTarget = object : Target {

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            }

            override fun onBitmapFailed(errorDrawable: Drawable?) {
                imageProgress.visibility = View.GONE
                image_error.visibility = View.VISIBLE
                extra_buttons.visibility = View.GONE
                toast("Failed to load the Image :(")
                fab_save.isEnabled = false
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {

                bitmap?.let { detail_image.setImageBitmap(it); mBitmap = it
                 imageProgress.visibility = View.GONE
                  detail_image.visibility = View.VISIBLE
                  extra_buttons.visibility = View.VISIBLE
                    fab_save.isEnabled = true
                }
            }

        }
        favorite.setOnClickListener {
            toggleFavourite(it,true)
            toast("Feature not implemented yet")
        }
        detail_image?.setOnClickListener {
            // Flip and set
            toggleFullscreen(!mFullScreen)
            mFullScreen = !mFullScreen
        }


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val response = intent.getParcelableExtra<Response?>(Constants.INTENT_RESPONSE_KEY)
        mHtmlLink =  response?.links?.html
        val sheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        val picasso = Picasso.with(this)
        response?.urls?.regular?.let { picasso.load(it).into(mTarget) }
        response?.color?.let {
            bottom_sheet.setBackgroundColor(Color.parseColor(it))

        }
        response?.id?.let { mImageName = it + ".jpg"; mImageId = it }
        val outValue = TypedValue()
        this.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        up_arrow.setBackgroundResource(outValue.resourceId);
        response?.user?.name.let { user_name.text = it ; mAuthorName = it}
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
                        fab_save.isEnabled = true
                        toast("Cannot set Wallpaper !")
                    }
                }
            }

        }


        up_arrow.setOnClickListener {
            if (sheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                up_arrow.rotation = up_arrow.rotation - 180
            } else if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                up_arrow.rotation = up_arrow.rotation - 180
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

        }

    private fun toggleFavourite(v:View,isFavourite:Boolean)
    {
        if(isFavourite)
        {
            v.setBackgroundResource(R.drawable.favorite_full)
        }
        else
        {
            v.setBackgroundResource(R.drawable.favorite_empty)
        }
    }



    private fun onSaveButtonClicked() {
        checkandAskforStorageWritePermission(ImagesDetailActivity@this,onSuccess = ::saveImageWrapper)
    }



    private fun getUtmLink(link:String):String
    {
        return Uri.parse(link).buildUpon().appendQueryParameter("utm_source","wlosh")
                .appendQueryParameter("utm_medium","referral").toString()


    }
    private fun onMoreButtonClicked()
    {
        mHtmlLink?.let {
            val link = getUtmLink(it)
            browse(link)

        }
    }

    private fun shareImage()
    {
        mImageId?.let { mUnSplashClient.notifyDownload(it) }
        mBitmap?.let {
            val bitmapPath = MediaStore.Images.Media.insertImage(contentResolver, it, "title", null)
            val bitmapUri = Uri.parse(bitmapPath)
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            val shareLink = mHtmlLink?.let { it1 -> getUtmLink(it1) }
            intent.putExtra(Intent.EXTRA_TEXT, "Picture by $mAuthorName. See more at $shareLink. \n Shared Via - Wlosh ")
            intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
            intent.type = "image/*"
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(Intent.createChooser(intent, "Share Image .."))
        }
    }
    private fun onShareButtonClicked()
    {
        checkandAskforStorageWritePermission(ImagesDetailActivity@this,::shareImage)

    }



    private fun saveImage():String?
    {
    mBitmap?.let {
        val direct = File(Environment.getExternalStorageDirectory().toString() + "/Wlosh");

        if (!direct.exists()) {
            direct.mkdirs()
        }

        val file = File(direct, mImageName);
        if (file.exists()) {
            file.delete();
        }
        try {
            val out = FileOutputStream (file)
            it.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file.absolutePath
    }
        return null
    }

    private fun saveImageWrapper() {
     val progressBar = indeterminateProgressDialog(message = "Please wait a bit…", title = "Saving Image")
    doAsync {
        val result:String? = saveImage()
        mImageId?.let { mUnSplashClient.notifyDownload(it) }
           uiThread {
               Log.d("Yolo","Result is $result");
               progressBar.dismiss();
               if(result == null)
                   toast("There was some error while saving Image")
                   else
                   longSnackbar(layout_container,"Saved Image at location : ${result}","Ok") {}

           }
            }
        }


    private fun initUnSplashClient() : UnSplashClient
    {
        return object: UnSplashClient(){
            override fun onFetchWallpapersFailure() {
            }

            override fun onFetchWallpapersSuccess(response: List<Response>) {
            }

            override fun onNotifyDownloadSuccess() {
            Log.i(TAG,"Downloaded image id $mImageId")
            }

            override fun onNotifyDownloadFailure() {
            Log.e(TAG,"Failed to ping image id $mImageId")
            }

        }
    }


}

