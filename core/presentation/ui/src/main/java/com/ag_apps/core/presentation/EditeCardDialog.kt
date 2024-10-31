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
import androidx.compose.ui.text.input.KeyboardType
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
fun EditeCardDialog(
    nameOnCardTextState: TextFieldState,
    cardNumberTextState: TextFieldState,
    expireDateTextState: TextFieldState,
    cvvTextState: TextFieldState,
    canSavingCard: Boolean,
    onSaveCard: () -> Unit,
    onDisclaimer: () -> Unit,
    onCardToggle: () -> Unit,
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
                        .clickable { onDisclaimer() }
                        .align(Alignment.End)
                )
                Text(
                    text = stringResource(R.string.payment_card),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                )
            }


            Spacer(Modifier.height(0.dp))

            ShopyTextField(
                textFieldState = nameOnCardTextState,
                hint = stringResource(R.string.card_holder_name_hint),
                title = stringResource(R.string.card_holder_name)
            )

            ShopyTextField(
                textFieldState = cardNumberTextState,
                hint = stringResource(R.string.card_number_hint),
                title = stringResource(R.string.card_number),
                keyBoardType = KeyboardType.Number
            )

            ShopyTextField(
                textFieldState = expireDateTextState,
                hint = stringResource(R.string.expire_date_hint),
                title = stringResource(R.string.expire_date),
                keyBoardType = KeyboardType.Number
            )

            ShopyTextField(
                textFieldState = cvvTextState,
                hint = stringResource(R.string.cvv_hint),
                title = stringResource(R.string.cvv),
                keyBoardType = KeyboardType.Number
            )

            Spacer(Modifier.height(0.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ShopyOutlinedButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.cancel),
                    onClick = { onCardToggle() }
                )

                ShopyButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.save),
                    enabled = canSavingCard,
                    onClick = { onSaveCard() }
                )
            }


        }

    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    ShopyTheme {
        EditeCardDialog(
            nameOnCardTextState = TextFieldState(),
            cardNumberTextState = TextFieldState(),
            expireDateTextState = TextFieldState(),
            cvvTextState = TextFieldState(),
            canSavingCard = true,
            onDisclaimer = {},
            onCardToggle = {},
            onSaveCard = {},
        )
    }
}