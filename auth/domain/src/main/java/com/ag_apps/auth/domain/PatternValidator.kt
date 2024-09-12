package com.ag_apps.auth.domain

/**
 * @author Ahmed Guedmioui
 */
interface PatternValidator {
    fun matches(value: String): Boolean
}