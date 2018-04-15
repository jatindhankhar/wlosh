package `in`.jatindhankhar.wlosh.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by jatin on 2/19/18.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class DownloadLinkResponse(
        val url: String? = null
) : Parcelable