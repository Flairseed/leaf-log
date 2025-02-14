package com.example.leaflog.core.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.leaflog.feature_authentication.data.remote.AuthService
import com.example.leaflog.ui.theme.LeafLogTheme

@Composable
fun AppDrawer(
    modifier: Modifier = Modifier,
    gotToLoginScreen: () -> Unit
) {
    val surfaceContainer = Color(0xFFF3EDE9)
    val onSurfaceVariant = Color(0xFF4D453E)
    val primary = Color(0xFF2E5B00)

    ModalDrawerSheet(
        modifier = modifier,
        drawerContainerColor = surfaceContainer
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .padding(top = 16.dp),
            text = AuthService.userName ?: "Not logged in",
            maxLines = 1,
            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
            overflow = TextOverflow.Ellipsis,
            color = onSurfaceVariant
        )
        HorizontalDivider(
            color = primary
        )
        NavigationDrawerItem(
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = surfaceContainer,
                unselectedContainerColor = surfaceContainer,
                selectedIconColor = onSurfaceVariant,
                unselectedIconColor = onSurfaceVariant,
                selectedTextColor = onSurfaceVariant,
                unselectedTextColor = onSurfaceVariant
            ),
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ExitToApp,
                    contentDescription = ""
                )
            },
            label = { Text(text = if (AuthService.isLoggedIn()) "Logout" else "Login") },
            selected = false,
            onClick = gotToLoginScreen
        )
        HorizontalDivider(
            color = primary
        )
    }
}

@Preview
@Composable
private fun PreviewAppDrawer() {
    LeafLogTheme {
        AppDrawer {}
    }
}