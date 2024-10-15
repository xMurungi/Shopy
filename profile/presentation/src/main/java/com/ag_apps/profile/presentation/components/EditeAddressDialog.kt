package com.ag_apps.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ag_apps.core.presentation.designsystem.components.ShopyButton
import com.ag_apps.core.presentation.designsystem.components.ShopyTextField
import com.ag_apps.profile.presentation.ProfileAction
import com.ag_apps.profile.presentation.ProfileState
import com.ag_apps.profile.presentation.R

/**
 * @author Ahmed Guedmioui
 */

@Composable
fun EditeAddressDialog(
    onAction: (ProfileAction) -> Unit,
    state: ProfileState,
    onDisclaimerClick: () -> Unit,
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
                            .clickable { onDisclaimerClick() }
                    )
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = stringResource(R.string.disclaimer),
                        modifier = Modifier
                            .size(25.dp)
                            .clickable { onAction(ProfileAction.OnAddressToggle) }
                    )
                }
                Text(
                    text = stringResource(R.string.shipping_address),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                )
            }


            Spacer(Modifier.height(0.dp))

            ShopyTextField(
                textFieldState = state.streetTextState,
                hint = stringResource(R.string.street_hint),
                title = stringResource(R.string.street),
            )

            ShopyTextField(
                textFieldState = state.cityTextState,
                hint = stringResource(R.string.city_hint),
                title = stringResource(R.string.city),
            )

            ShopyTextField(
                textFieldState = state.regionTextState,
                hint = stringResource(R.string.state_province_region_hint),
                title = stringResource(R.string.state_province_region),
            )

            ShopyTextField(
                textFieldState = state.zipcodeTextState,
                hint = stringResource(R.string.zip_code_postal_code_hint),
                title = stringResource(R.string.zip_code_postal_code),
            )

            ShopyTextField(
                textFieldState = state.countryTextState,
                hint = stringResource(R.string.country_hint),
                title = stringResource(R.string.country),
            )

            Spacer(Modifier.height(0.dp))

            ShopyButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.save_address),
                enabled = state.canSavingAddress,
                onClick = { onAction(ProfileAction.OnSaveAddress) }
            )

        }

    }
}
