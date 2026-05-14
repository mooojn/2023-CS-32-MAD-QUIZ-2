package com.example.mad_eval.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Article(
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("url")
    val url: String,
    @SerializedName("image")
    val image: String?,
    @SerializedName("publishedAt")
    val publishedAt: String,
    @SerializedName("source")
    val source: Source
) : Parcelable {

    constructor(parcel: Parcel) : this(
        title = parcel.readString().orEmpty(),
        description = parcel.readString(),
        content = parcel.readString(),
        url = parcel.readString().orEmpty(),
        image = parcel.readString(),
        publishedAt = parcel.readString().orEmpty(),
        source = parcel.readParcelable(Source::class.java.classLoader) ?: Source("", "")
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(content)
        parcel.writeString(url)
        parcel.writeString(image)
        parcel.writeString(publishedAt)
        parcel.writeParcelable(source, flags)
    }

    override fun describeContents(): Int = 0

    data class Source(
        @SerializedName("name")
        val name: String,
        @SerializedName("url")
        val url: String
    ) : Parcelable {

        constructor(parcel: Parcel) : this(
            name = parcel.readString().orEmpty(),
            url = parcel.readString().orEmpty()
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(name)
            parcel.writeString(url)
        }

        override fun describeContents(): Int = 0

        companion object CREATOR : Parcelable.Creator<Source> {
            override fun createFromParcel(parcel: Parcel): Source = Source(parcel)
            override fun newArray(size: Int): Array<Source?> = arrayOfNulls(size)
        }
    }

    companion object CREATOR : Parcelable.Creator<Article> {
        override fun createFromParcel(parcel: Parcel): Article = Article(parcel)
        override fun newArray(size: Int): Array<Article?> = arrayOfNulls(size)
    }
}
