package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BizBalanceLogoHorizontal
import com.example.ui.components.BizBalanceLogoVertical
import com.example.ui.components.BizBalanceSymbol
import com.example.ui.components.BrandingFooter
import com.example.ui.theme.BizGreenAccent
import com.example.ui.theme.BizGreenLightBg
import com.example.ui.theme.BizGreenPrimary
import com.example.ui.theme.BizGreenSecondary
import com.example.ui.theme.BizSlateCardDark
import com.example.ui.theme.BizSlateDarkBg

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BrandKitScreen(
    onShowSplash: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var selectedTab by remember { mutableIntStateOf(0) }

    val exportFormats = listOf("SVG", "PNG", "PDF")
    val dimensions = listOf("1024x1024", "512x512", "256x256", "192x192", "180x180", "64x64")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .testTag("brand_kit_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header Banner
        item {
            Surface(
                color = BizSlateDarkBg,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BizBalanceLogoVertical(isDarkBg = true, symbolSize = 72.dp, showSlogan = true)

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = BizGreenSecondary,
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = BizGreenAccent,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Brand Guidelines 2026",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        Button(
                            onClick = onShowSplash,
                            colors = ButtonDefaults.buttonColors(containerColor = BizGreenPrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Ver Splash Screen", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Color Palette Section
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Palette, contentDescription = null, tint = BizGreenPrimary)
                        Text(
                            text = "Paleta de Cores Oficial",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BizGreenSecondary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ColorChip(name = "Primary Green", hex = "#16A34A", color = BizGreenPrimary)
                        ColorChip(name = "Dark Green", hex = "#14532D", color = BizGreenSecondary)
                        ColorChip(name = "Light Green", hex = "#22C55E", color = BizGreenAccent)
                        ColorChip(name = "White", hex = "#FFFFFF", color = Color.White, isLight = true)
                        ColorChip(name = "Dark Background", hex = "#0F172A", color = BizSlateDarkBg)
                    }
                }
            }
        }

        // Brand Variants Navigation Tabs
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = BizGreenPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = BizGreenPrimary
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Logótipos", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("App Icon & Favicon", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Exportar Assets", fontWeight = FontWeight.Bold) }
                )
            }
        }

        // Tab Content
        when (selectedTab) {
            0 -> {
                // Logos Variants
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        BrandCard(
                            title = "Main Logo (Versão Horizontal Dark)",
                            subtitle = "Para cabeçalhos, banners e splash escura"
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(BizSlateDarkBg, RoundedCornerShape(12.dp))
                                    .padding(20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                BizBalanceLogoHorizontal(isDarkBg = true, symbolSize = 44.dp, showSlogan = true)
                            }
                        }

                        BrandCard(
                            title = "Main Logo (Versão Horizontal Light)",
                            subtitle = "Para documentos, faturas e fundo claro"
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
                                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                                    .padding(20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                BizBalanceLogoHorizontal(isDarkBg = false, symbolSize = 44.dp, showSlogan = true)
                            }
                        }

                        BrandCard(
                            title = "Versão Vertical (Centered)",
                            subtitle = "Ideal para splash, capas de relatórios e totens"
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(BizSlateDarkBg, RoundedCornerShape(12.dp))
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                BizBalanceLogoVertical(isDarkBg = true, symbolSize = 64.dp, showSlogan = true)
                            }
                        }

                        BrandCard(
                            title = "Versão Monocromática (High Contrast)",
                            subtitle = "Para impressão 1-cor e bordados corporativos"
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Black, RoundedCornerShape(12.dp))
                                    .padding(20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "BIZBALANCE CEO AI",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 2.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            1 -> {
                // App Icon, Favicon, Social Media
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        BrandCard(
                            title = "App Icon Oficial (Android / iOS / Web)",
                            subtitle = "Símbolo isolado sem texto • Fundo Dark #0F172A"
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    BizBalanceSymbol(size = 80.dp)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("512x512", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    BizBalanceSymbol(size = 56.dp)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("192x192", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    BizBalanceSymbol(size = 40.dp)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("180x180", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        BrandCard(
                            title = "Favicon & Avatar para Redes Sociais",
                            subtitle = "Otimizado para perfis corporativos e navegação web"
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .size(72.dp)
                                            .clip(CircleShape)
                                            .background(BizSlateDarkBg),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        BizBalanceSymbol(size = 56.dp)
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("Social Avatar (1024x1024)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    BizBalanceSymbol(size = 32.dp)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("Favicon (64x64)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // Export Formats & Resolution Matrix
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        BrandCard(
                            title = "Formatos & Resoluções de Exportação",
                            subtitle = "Selecione o formato para baixar ou copiar o pacote de assets"
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text(
                                    text = "Formatos Suportados:",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    exportFormats.forEach { fmt ->
                                        Surface(
                                            color = BizGreenLightBg,
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Text(
                                                text = "✓ $fmt",
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    color = BizGreenSecondary,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "Resoluções Disponíveis:",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                                )

                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    dimensions.forEach { dim ->
                                        Surface(
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.clickable {
                                                Toast.makeText(context, "Asset $dim preparado!", Toast.LENGTH_SHORT).show()
                                            }
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Download,
                                                    contentDescription = null,
                                                    tint = BizGreenPrimary,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(dim, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Button(
                                    onClick = {
                                        val svgData = """
                                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512">
                                              <!-- BizBalance CEO AI Official Logo SVG -->
                                              <rect width="512" height="512" rx="128" fill="#0F172A"/>
                                              <path d="M120 120 h120 c40 0 70 20 70 55 c0 25 -15 45 -40 50 c35 5 50 30 50 60 c0 40 -35 65 -80 65 h-120 z" fill="#16A34A"/>
                                              <circle cx="380" cy="150" r="24" fill="#22C55E"/>
                                              <path d="M220 300 L300 220 L350 270 L430 180" stroke="#22C55E" stroke-width="24" stroke-linecap="round"/>
                                            </svg>
                                        """.trimIndent()
                                        clipboardManager.setText(AnnotatedString(svgData))
                                        Toast.makeText(context, "Código SVG copiado para a área de transferência!", Toast.LENGTH_LONG).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = BizGreenPrimary),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Copiar Marcação SVG Vetorial")
                                }
                            }
                        }
                    }
                }
            }
        }

        // Founder & Copyright Footer
        item {
            BrandingFooter()
        }
    }
}

@Composable
private fun ColorChip(
    name: String,
    hex: String,
    color: Color,
    isLight: Boolean = false
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(color)
                    .then(
                        if (isLight) Modifier.border(1.dp, Color(0xFFCBD5E1), CircleShape) else Modifier
                    )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(name, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                Text(hex, style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B), fontSize = 10.sp))
            }
        }
    }
}

@Composable
private fun BrandCard(
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = BizGreenSecondary
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}
