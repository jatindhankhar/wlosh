package `in`.jatindhankhar.wlosh.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@SuppressLint("ParcelCreator")
@Parcelize
data class Response(
        val currentUserCollections: @RawValue List<Any?>? = null,
        val color: String? = null,
        val createdAt: String? = null,
        val description: @RawValue Any? = null,
        val sponsored: Boolean? = null,
        val likedByUser: Boolean? = null,
        val urls: Urls? = null,
        val updatedAt: String? = null,
        val width: Int? = null,
        val links: Links? = null,
        val id: String? = null,
        val categories: @RawValue List<Any?>? = null,
        val user: User? = null,
        val height: Int? = null,
        val likes: Int? = null
) : Parcelable
