package `in`.jatindhankhar.wlosh.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Urls(
	val small: String? = null,
	val thumb: String? = null,
	val raw: String? = null,
	val regular: String? = null,
	val full: String? = null
) : Parcelable
