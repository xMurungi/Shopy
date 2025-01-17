package com.ag_apps.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/**
 * @author Ahmed Guedmioui
 */
@Composable
fun ShopyDialog(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    descriptionTextAlign: TextAlign = TextAlign.Center,
    onDismiss: () -> Unit = {},
    properties: DialogProperties = DialogProperties(),
    primaryButton: @Composable RowScope.() -> Unit = {},
    secondaryButton: @Composable RowScope.() -> Unit = {},
) {
    Dialog(
        properties = properties,
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                fontSize = 23.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = description,
                textAlign = descriptionTextAlign,
                fontSize = 17.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                secondaryButton()
                primaryButton()
            }
        }
    }
}















