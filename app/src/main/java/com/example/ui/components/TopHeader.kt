package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BizGreenLightBg
import com.example.ui.theme.BizGreenPrimary
import com.example.ui.theme.BizGreenSecondary

@Composable
fun TopHeader(
    planName: String,
    isAdminMode: Boolean,
    onToggleAdmin: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenBrandKit: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("top_header"),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .clickable { onOpenBrandKit() }
                    .weight(1f)
            ) {
                BizBalanceLogoHorizontal(
                    isDarkBg = false,
                    symbolSize = 36.dp,
                    showSlogan = false
                )
                
                Text(
                    text = "O CEO Inteligente que Ajuda a Gerir o Seu Negócio",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Plan Badge
                Surface(
                    color = BizGreenLightBg,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("plan_badge")
                ) {
                    Text(
                        text = planName.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = BizGreenPrimary,
                            fontSize = 10.sp
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Brand Kit button
                IconButton(
                    onClick = onOpenBrandKit,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(BizGreenLightBg)
                        .testTag("brand_kit_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Palette,
                        contentDescription = "Brand Kit & Logo Assets",
                        tint = BizGreenPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Admin mode toggle button
                IconButton(
                    onClick = onToggleAdmin,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            if (isAdminMode) BizGreenSecondary else BizGreenLightBg
                        )
                        .testTag("admin_toggle_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = "Modo Admin",
                        tint = if (isAdminMode) Color.White else BizGreenPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Settings button
                IconButton(
                    onClick = onOpenSettings,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(BizGreenLightBg)
                        .testTag("settings_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Ajustes",
                        tint = BizGreenSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
