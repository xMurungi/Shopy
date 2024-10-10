package com.ag_apps.shopy

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ag_apps.core.domain.UserDataSource
import kotlinx.coroutines.launch

/**
 * @author Ahmed Guedmioui
 */
class MainViewModel(
    private val userDataSource: UserDataSource
) : ViewModel() {

    var state by mutableStateOf(MainState())
        private set

    init {
        viewModelScope.launch {
            state = state.copy(isCheckingAuth = true)

            state = state.copy(
                isLoggedIn = userDataSource.isLoggedIn()
            )

            state = state.copy(isCheckingAuth = false)
        }
    }

}





















