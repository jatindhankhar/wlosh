package `in`.jatindhankhar.wlosh.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class ProfileImage(

	val small: String? = null,
	val large: String? = null,
	val medium: String? = null
) : Parcelable
