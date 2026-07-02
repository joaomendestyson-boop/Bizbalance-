package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SubscriptionEntity
import com.example.ui.components.BrandingFooter
import com.example.ui.theme.BizGreenAccent
import com.example.ui.theme.BizGreenLightBg
import com.example.ui.theme.BizGreenPrimary
import com.example.ui.theme.BizGreenSecondary

data class PlanOffer(
    val name: String,
    val priceText: String,
    val priceVal: Double,
    val description: String,
    val isPopular: Boolean = false,
    val features: List<String>
)

@Composable
fun SubscriptionScreen(
    currentSubscription: SubscriptionEntity,
    onOpenStripeCheckout: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val plans = listOf(
        PlanOffer(
            name = "Free",
            priceText = "R$ 0 /mês",
            priceVal = 0.0,
            description = "Ideal para testes e microempreendedores.",
            features = listOf(
                "Até 30 lançamentos por mês",
                "Dashboard financeiro essencial",
                "Relatórios em tela"
            )
        ),
        PlanOffer(
            name = "Pro",
            priceText = "R$ 49 /mês",
            priceVal = 49.0,
            description = "Para pequenas empresas em crescimento acelerado.",
            isPopular = true,
            features = listOf(
                "Lançamentos ilimitados",
                "Assistente IA BizBalance",
                "Exportação de relatórios PDF/CSV",
                "Até 3 membros na equipe"
            )
        ),
        PlanOffer(
            name = "Business",
            priceText = "R$ 99 /mês",
            priceVal = 99.0,
            description = "Gestão financeira completa com CRM de clientes.",
            features = listOf(
                "Tudo do plano Pro",
                "Módulo CRM + Gestão de Dívidas",
                "Análise avançada de fluxo de caixa",
                "Até 10 membros na equipe"
            )
        ),
        PlanOffer(
            name = "Enterprise",
            priceText = "R$ 249 /mês",
            priceVal = 249.0,
            description = "Para médias empresas, franquias e corporativo.",
            features = listOf(
                "Tudo do plano Business",
                "Painel Administrativo Completo",
                "Acesso a APIs & Integrações",
                "Suporte prioritário e auditoria"
            )
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Subscription Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("current_plan_card"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = BizGreenSecondary)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "PLANO ATIVO NO BIZBALANCE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.8f),
                                letterSpacing = 1.sp
                            )
                        )

                        Surface(
                            color = BizGreenAccent,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = currentSubscription.status,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 10.sp
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Plano ${currentSubscription.planName}",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )

                    Text(
                        text = "Incluso: Assistente IA, CRM de Clientes, Relatórios Executivos",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 11.sp
                        ),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }

        item {
            Text(
                text = "Escolha o Plano Ideal para seu Negócio",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )
        }

        items(plans) { plan ->
            val isCurrent = currentSubscription.planName.equals(plan.name, ignoreCase = true)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("plan_card_${plan.name}"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (plan.isPopular) BizGreenLightBg else MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = if (plan.isPopular) 4.dp else 1.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = plan.name,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = BizGreenSecondary
                                    )
                                )

                                if (plan.isPopular) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        color = BizGreenPrimary,
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text(
                                            text = "MAIS POPULAR",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 9.sp
                                            ),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }

                            Text(
                                text = plan.description,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 12.sp
                                )
                            )
                        }

                        Text(
                            text = plan.priceText,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = BizGreenPrimary,
                                fontSize = 18.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    plan.features.forEach { feat ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = BizGreenPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = feat,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isCurrent) {
                        OutlinedButton(
                            onClick = {},
                            enabled = false,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Plano Atual em Uso")
                        }
                    } else {
                        Button(
                            onClick = { onOpenStripeCheckout(plan.name) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_select_plan_${plan.name}"),
                            colors = ButtonDefaults.buttonColors(containerColor = BizGreenPrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.CreditCard, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Assinar via Stripe", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item {
            BrandingFooter()
        }
    }
}
