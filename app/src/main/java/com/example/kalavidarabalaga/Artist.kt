package com.example.kalavidarabalaga

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Artist(
    val name: String = "",
    val district: String = "",
    val location: String = "", // Specific location like Taluk or Village
    val artType: String = "",
    val phone: String = "",
    val imageUrl: String = "",
    val description: String = "" // Added for dance form details
) : Parcelable
