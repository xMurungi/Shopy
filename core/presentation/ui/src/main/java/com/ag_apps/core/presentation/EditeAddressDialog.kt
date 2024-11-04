package com.ag_apps.core.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ag_apps.core.presentation.designsystem.ShopyTheme
import com.ag_apps.core.presentation.designsystem.components.ShopyButton
import com.ag_apps.core.presentation.designsystem.components.ShopyOutlinedButton
import com.ag_apps.core.presentation.designsystem.components.ShopyTextField
import com.ag_apps.core.presentation.ui.R

/**
 * @author Ahmed Guedmioui
 */

@Composable
fun EditeAddressDialog(
    streetTextState: TextFieldState,
    cityTextState: TextFieldState,
    regionTextState: TextFieldState,
    zipcodeTextState: TextFieldState,
    countryTextState: TextFieldState,
    canSavingAddress: Boolean,
    isSavingAddress: Boolean,
    onDisclaimerClick: () -> Unit,
    onSaveAddress: () -> Unit,
    onAddressToggle: () -> Unit
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
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = stringResource(R.string.disclaimer),
                    modifier = Modifier
                        .clickable { onDisclaimerClick() }
                        .align(Alignment.End)
                )
                Text(
                    text = stringResource(R.string.shipping_address),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                )
            }


            Spacer(Modifier.height(0.dp))

            ShopyTextField(
                textFieldState = streetTextState,
                hint = stringResource(R.string.street_hint),
                title = stringResource(R.string.street),
            )

            ShopyTextField(
                textFieldState = cityTextState,
                hint = stringResource(R.string.city_hint),
                title = stringResource(R.string.city),
            )

            ShopyTextField(
                textFieldState = regionTextState,
                hint = stringResource(R.string.state_province_region_hint),
                title = stringResource(R.string.state_province_region),
            )

            ShopyTextField(
                textFieldState = zipcodeTextState,
                hint = stringResource(R.string.zip_code_postal_code_hint),
                title = stringResource(R.string.zip_code_postal_code),
            )

            ShopyTextField(
                textFieldState = countryTextState,
                hint = stringResource(R.string.country_hint),
                title = stringResource(R.string.country),
            )

            Spacer(Modifier.height(0.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ShopyOutlinedButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.cancel),
                    onClick = { onAddressToggle() }
                )


                ShopyButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.save),
                    enabled = canSavingAddress && !isSavingAddress,
                    isLoading = isSavingAddress,
                    onClick = { onSaveAddress() }
                )
            }


        }

    }
}

@Preview
@Composable
private fun EditeAddressDialogPreview() {
    ShopyTheme {
        EditeAddressDialog(
            streetTextState = TextFieldState(),
            cityTextState = TextFieldState(),
            regionTextState = TextFieldState(),
            zipcodeTextState = TextFieldState(),
            countryTextState = TextFieldState(),
            canSavingAddress = true,
            isSavingAddress = true,
            onDisclaimerClick = {},
            onSaveAddress = {},
            onAddressToggle = {},
        )
    }
}