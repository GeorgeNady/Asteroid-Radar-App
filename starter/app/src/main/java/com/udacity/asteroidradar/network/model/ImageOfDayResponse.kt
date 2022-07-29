package com.udacity.asteroidradar.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.db.entities.ImageOfDayTable

@JsonClass(generateAdapter = true)
data class ImageOfDayResponse(
    val url: String,
    @Json(name = "media_type") val mediaType: String,
    val title: String,
) {

    fun asDatabaseModel() = ImageOfDayTable(null, url, mediaType , title)

}
