package com.researchflow.app.core.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

fun Timestamp.toDisplayDateTime(): String {
    val formatter = SimpleDateFormat(
        "dd MMM yyyy, h:mm a",
        Locale.getDefault()
    )

    return formatter.format(this.toDate())
}

fun Timestamp.toDisplayDate(): String {
    val formatter = SimpleDateFormat(
        "dd MMM yyyy",
        Locale.getDefault()
    )

    return formatter.format(this.toDate())
}

