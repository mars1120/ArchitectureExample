package com.marschen.architectureexample.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = "drama",
    indices = [Index(value = ["name"], unique = false)]
)
data class Drama(
    @SerializedName("drama_id")
    var drama_id: String,
    @SerializedName("name")
    var name: String?,
    @SerializedName("total_views")
    var total_views: Int,
    @SerializedName("created_at")
    var created_at: String?,
    @SerializedName("thumb")
    var thumb: String?,
    @SerializedName("rating")
    var rating: Double
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var indexInResponse: Int = -1
}