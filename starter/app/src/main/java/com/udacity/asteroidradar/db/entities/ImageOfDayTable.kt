package com.udacity.asteroidradar.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.domain.ImageOfDay

@Entity(tableName = "image_day_table")
data class ImageOfDayTable(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val url: String,
    @ColumnInfo(name = "media_type") val mediaType: String,
    val title: String,
) {

    fun asDomainModel() = ImageOfDay(url, mediaType, title)

}

