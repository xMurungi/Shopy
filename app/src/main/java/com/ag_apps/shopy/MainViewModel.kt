package com.ag_apps.shopy

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ag_apps.core.domain.SessionStorage
import kotlinx.coroutines.launch

/**
 * @author Ahmed Guedmioui
 */
class MainViewModel(
    private val sessionStorage: SessionStorage
) : ViewModel() {

    var state by mutableStateOf(MainState())
        private set

    init {
        viewModelScope.launch {
            state = state.copy(isCheckingAuth = true)

            val isLoggedIn = sessionStorage.get() != null

            state = state.copy(
                isLoggedIn = isLoggedIn
            )

            state = state.copy(isCheckingAuth = false)
        }
    }

}





















