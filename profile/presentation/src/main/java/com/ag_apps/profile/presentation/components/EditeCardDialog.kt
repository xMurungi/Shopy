package com.ag_apps.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ag_apps.core.domain.User
import com.ag_apps.core.presentation.designsystem.ShopyTheme
import com.ag_apps.core.presentation.designsystem.components.ShopyButton
import com.ag_apps.core.presentation.designsystem.components.ShopyTextField
import com.ag_apps.profile.presentation.ProfileAction
import com.ag_apps.profile.presentation.ProfileState
import com.ag_apps.profile.presentation.R

/**
 * @author Ahmed Guedmioui
 */

@Composable
fun EditeCardDialog(
    onAction: (ProfileAction) -> Unit,
    state: ProfileState,
    onDisclaimer: () -> Unit,
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = stringResource(R.string.disclaimer),
                        modifier = Modifier
                            .clickable { onDisclaimer() }
                    )
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = stringResource(R.string.disclaimer),
                        modifier = Modifier
                            .size(25.dp)
                            .clickable { onAction(ProfileAction.OnCardToggle) }
                    )
                }
                Text(
                    text = stringResource(R.string.payment_card),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                )
            }


            Spacer(Modifier.height(0.dp))

            ShopyTextField(
                textFieldState = state.nameOnCardTextState,
                hint = stringResource(R.string.card_holder_name_hint),
                title = stringResource(R.string.card_holder_name)
            )

            ShopyTextField(
                textFieldState = state.cardNumberTextState,
                hint = stringResource(R.string.card_number_hint),
                title = stringResource(R.string.card_number),
                keyBoardType = KeyboardType.Number
            )

            ShopyTextField(
                textFieldState = state.expireDateTextState,
                hint = stringResource(R.string.expire_date_hint),
                title = stringResource(R.string.expire_date),
                keyBoardType = KeyboardType.Number
            )

            ShopyTextField(
                textFieldState = state.cvvTextState,
                hint = stringResource(R.string.cvv_hint),
                title = stringResource(R.string.cvv),
                keyBoardType = KeyboardType.Number
            )

            Spacer(Modifier.height(0.dp))

            ShopyButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.save_card),
                enabled = state.canSavingCard,
                onClick = { onAction(ProfileAction.OnSaveCard) }
            )

        }

    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    ShopyTheme {
        EditeCardDialog(
            state = ProfileState(
                user = User(
                    name = "Ahmed Guedmioui",
                    image = "",
                    email = "ahmed@gmail.com",
                    userId = "",
                    address = null,
                    cart = emptyList(),
                    wishlist = emptyList(),
                ),
                isEditeCardShowing = true
            ),
            onAction = {},
            onDisclaimer = {},
        )
    }
}