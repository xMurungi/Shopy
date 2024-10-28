package com.ag_apps.core.presentation.util

/**
 * @author Ahmed Guedmioui
 */

fun Double.originalPrice(discount: Int): Double {
    return this * (100 + discount) / 100
}