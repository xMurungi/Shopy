import com.ag_apps.core.presentation.ui.UiText


/**
 * @author Ahmed Guedmioui
 */
sealed interface RegisterEvent {
    data object RegistrationSuccess: RegisterEvent
    data class Error(val error: UiText): RegisterEvent
}

















