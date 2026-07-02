package com.example.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BizGreenLightBg
import com.example.ui.theme.BizGreenPrimary
import com.example.ui.theme.BizGreenSecondary

data class NavItem(
    val title: String,
    val icon: ImageVector,
    val index: Int,
    val testTag: String
)

@Composable
fun BizBalanceBottomBar(
    selectedIndex: Int,
    isAdminMode: Boolean,
    onSelectTab: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = mutableListOf(
        NavItem("Home", Icons.Default.Home, 0, "nav_item_home"),
        NavItem("Lançamentos", Icons.Default.ReceiptLong, 1, "nav_item_transactions"),
        NavItem("CRM", Icons.Default.People, 2, "nav_item_crm"),
        NavItem("CEO AI", Icons.Default.AutoAwesome, 3, "nav_item_ai"),
        NavItem("Relatórios", Icons.Default.Assessment, 4, "nav_item_reports"),
        NavItem("Brand Kit", Icons.Default.Palette, 7, "nav_item_brand_kit")
    )

    if (isAdminMode) {
        items.add(NavItem("Admin", Icons.Default.AdminPanelSettings, 6, "nav_item_admin"))
    }

    NavigationBar(
        modifier = modifier.testTag("bizbalance_bottom_nav"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    ) {
        items.forEach { item ->
            val isSelected = selectedIndex == item.index

            NavigationBarItem(
                selected = isSelected,
                onClick = { onSelectTab(item.index) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 10.sp
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BizGreenPrimary,
                    selectedTextColor = BizGreenPrimary,
                    indicatorColor = BizGreenLightBg,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag(item.testTag)
            )
        }
    }
}
