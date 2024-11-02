package com.ag_apps.core.domain.util

/**
 * @author Ahmed Guedmioui
 */
sealed interface DataError: Error {
    enum class Network: DataError {
        REQUEST_TIMEOUT,
        UNAUTHORIZED,
        CONFLICT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        SERIALIZATION_ERROR,
        PARSING_ERROR,
        NOT_FOUND,
        UNEXPECTED_TYPE,
        CANCELED,
        PAYMENT_FAILED,
        UNKNOWN
    }

    enum class Local: DataError {
        DISK_FULL
    }
}