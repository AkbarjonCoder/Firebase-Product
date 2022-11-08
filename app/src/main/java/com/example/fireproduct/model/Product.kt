package com.example.fireproduct.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Product(
    val name: String = "",
    val price: String = ""
): Parcelable
