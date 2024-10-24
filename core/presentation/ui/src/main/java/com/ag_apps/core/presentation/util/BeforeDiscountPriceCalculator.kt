package com.ag_apps.core.presentation.util

/**
 * @author Ahmed Guedmioui
 */

fun Float.originalPrice(discount: Int): Float {
    return this * (100 + discount) / 100
}