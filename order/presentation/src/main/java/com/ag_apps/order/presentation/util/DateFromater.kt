package com.ag_apps.order.presentation.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * @author Ahmed Guedmioui
 */
fun Long.toFormattedDate(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return format.format(date)

}