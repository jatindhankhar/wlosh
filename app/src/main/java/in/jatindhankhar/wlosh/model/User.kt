package `in`.jatindhankhar.wlosh.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class User(
        val totalPhotos: Int? = null,
        val twitterUsername: String? = null,
        val lastName: String? = null,
        val bio: String? = null,
        val totalLikes: Int? = null,
        val portfolioUrl: String? = null,
        @SerializedName("profile_image")
        val profileImage: ProfileImage? = null,
        val updatedAt: String? = null,
        val name: String? = null,
        val location: String? = null,
        val links: Links? = null,
        val totalCollections: Int? = null,
        val id: String? = null,
        val firstName: String? = null,
        val username: String? = null
) : Parcelable
