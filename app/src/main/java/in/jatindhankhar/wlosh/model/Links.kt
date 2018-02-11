package `in`.jatindhankhar.wlosh.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Links(
	val followers: String? = null,
	val portfolio: String? = null,
	val following: String? = null,
	val self: String? = null,
	val html: String? = null,
	val photos: String? = null,
	val likes: String? = null
) : Parcelable
