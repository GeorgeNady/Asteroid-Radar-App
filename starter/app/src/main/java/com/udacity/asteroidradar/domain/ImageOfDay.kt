package com.udacity.asteroidradar.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageOfDay(
    val url: String,
    val mediaType: String,
    val title: String,
) : Parcelable
