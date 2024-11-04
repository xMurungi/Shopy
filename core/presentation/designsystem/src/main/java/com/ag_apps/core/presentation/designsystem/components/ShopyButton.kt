package com.ag_apps.core.presentation.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * @author Ahmed Guedmioui
 */
@Composable
fun ShopyButton(
    modifier: Modifier = Modifier,
    text: String? = null,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable () -> Unit = {}
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = RoundedCornerShape(100.dp),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.padding(vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 1.5.dp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            if (text != null) {
                Text(
                    text = text,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onPrimary.copy(if (isLoading) 0f else 1f)
                )
            } else {
                content()
            }
        }
    }
}


@Composable
fun ShopyOutlinedButton(
    modifier: Modifier = Modifier,
    text: String? = null,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    verticalPadding: Dp = 4.dp,
    onClick: () -> Unit,
    content: @Composable () -> Unit = {}
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.onBackground,
        ),
        border = BorderStroke(
            width = 0.5.dp,
            color = if (enabled) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.primary.copy(0.3f)
        ),
        shape = RoundedCornerShape(100.dp),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.padding(vertical = verticalPadding),
            contentAlignment = Alignment.Center
        ) {
            if (text != null) {
                Text(
                    text = text,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.alpha(if (isLoading) 0f else 1f),
                )
            } else {
                Box(modifier = Modifier.alpha(if (isLoading) 0f else 1f)) {
                    content()
                }
            }

            CircularProgressIndicator(
                strokeWidth = 1.5.dp,
                modifier = Modifier
                    .size(15.dp)
                    .alpha(if (isLoading) 1f else 0f)
            )
        }
    }
}

















