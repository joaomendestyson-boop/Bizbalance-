package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.BizGreenAccent
import com.example.ui.theme.BizGreenPrimary
import com.example.ui.theme.BizGreenSecondary
import com.example.ui.theme.BizSlateDarkBg

/**
 * Official Logo Symbol Component for BizBalance CEO AI
 */
@Composable
fun BizBalanceSymbol(
    size: Dp = 48.dp,
    showBorder: Boolean = true,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(size * 0.28f))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(BizSlateDarkBg, Color(0xFF1E293B))
                )
            )
            .then(
                if (showBorder) Modifier.border(
                    width = 1.5.dp,
                    color = BizGreenAccent.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(size * 0.28f)
                ) else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_bizbalance_logo_1783030244166),
            contentDescription = "BizBalance CEO AI Logo Symbol",
            modifier = Modifier
                .size(size * 0.85f)
                .clip(RoundedCornerShape(size * 0.22f))
        )
    }
}

/**
 * Horizontal Official Logo for BizBalance CEO AI
 */
@Composable
fun BizBalanceLogoHorizontal(
    isDarkBg: Boolean = false,
    symbolSize: Dp = 40.dp,
    showSlogan: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.testTag("bizbalance_logo_horizontal"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        BizBalanceSymbol(size = symbolSize)

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val annotatedString = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = if (isDarkBg) Color.White else BizGreenSecondary,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = (symbolSize.value * 0.52f).sp
                        )
                    ) {
                        append("Biz")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = BizGreenPrimary,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = (symbolSize.value * 0.52f).sp
                        )
                    ) {
                        append("Balance")
                    }
                }

                Text(
                    text = annotatedString,
                    modifier = Modifier.testTag("logo_text_brand")
                )

                Surface(
                    color = BizGreenAccent,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .testTag("logo_ceo_ai_badge")
                ) {
                    Text(
                        text = "CEO AI",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = (symbolSize.value * 0.26f).sp
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            if (showSlogan) {
                Text(
                    text = "O CEO Inteligente que Ajuda a Gerir o Seu Negócio",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (isDarkBg) Color(0xFF94A3B8) else Color(0xFF64748B),
                        fontSize = (symbolSize.value * 0.26f).sp,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(top = 1.dp)
                )
            }
        }
    }
}

/**
 * Vertical / Centered Official Logo for BizBalance CEO AI
 */
@Composable
fun BizBalanceLogoVertical(
    isDarkBg: Boolean = true,
    symbolSize: Dp = 80.dp,
    showSlogan: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.testTag("bizbalance_logo_vertical"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BizBalanceSymbol(size = symbolSize, showBorder = true)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val annotatedString = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = if (isDarkBg) Color.White else BizGreenSecondary,
                            fontWeight = FontWeight.Black,
                            fontSize = 28.sp
                        )
                    ) {
                        append("Biz")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = BizGreenPrimary,
                            fontWeight = FontWeight.Black,
                            fontSize = 28.sp
                        )
                    ) {
                        append("Balance")
                    }
                }

                Text(text = annotatedString)

                Surface(
                    color = BizGreenAccent,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "CEO AI",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            if (showSlogan) {
                Text(
                    text = "O CEO Inteligente que Ajuda a Gerir o Seu Negócio.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (isDarkBg) Color(0xFF94A3B8) else Color(0xFF475569),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
}
