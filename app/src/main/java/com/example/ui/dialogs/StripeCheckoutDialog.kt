package com.example.ui.dialogs

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BizGreenPrimary
import com.example.ui.theme.BizGreenSecondary

@Composable
fun StripeCheckoutDialog(
    planName: String,
    onDismiss: () -> Unit,
    onConfirmUpgrade: (planName: String, price: Double) -> Unit
) {
    val context = LocalContext.current
    val planPrice = when (planName) {
        "Enterprise" -> 249.0
        "Business" -> 99.0
        "Pro" -> 49.0
        else -> 0.0
    }

    var cardNumber by remember { mutableStateOf("4242 •••• •••• 4242") }
    var expiryDate by remember { mutableStateOf("12/28") }
    var cvcCode by remember { mutableStateOf("123") }
    var cardHolder by remember { mutableStateOf("JOAO G MENDES") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Checkout Seguro Stripe",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = BizGreenSecondary,
                        fontSize = 18.sp
                    )
                )

                Surface(
                    color = BizGreenPrimary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = BizGreenPrimary, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("SSL 256-bit", fontSize = 10.sp, color = BizGreenPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Assinatura Plano $planName", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Cobrança mensal recorrente com cancelamento simples", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text("R$ ${String.format("%.2f", planPrice)}/mês", fontWeight = FontWeight.Bold, color = BizGreenPrimary)
                    }
                }

                OutlinedTextField(
                    value = cardHolder,
                    onValueChange = { cardHolder = it },
                    label = { Text("Nome Impresso no Cartão") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("stripe_card_holder"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = cardNumber,
                    onValueChange = { cardNumber = it },
                    label = { Text("Número do Cartão de Crédito") },
                    leadingIcon = { Icon(imageVector = Icons.Default.CreditCard, contentDescription = null, tint = BizGreenPrimary) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("stripe_card_number"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = expiryDate,
                        onValueChange = { expiryDate = it },
                        label = { Text("Validade (MM/AA)") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("stripe_card_expiry"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = cvcCode,
                        onValueChange = { cvcCode = it },
                        label = { Text("CVC / CVV") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("stripe_card_cvc"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    Toast.makeText(context, "Pagamento aprovado via Stripe! Plano $planName ativado.", Toast.LENGTH_LONG).show()
                    onConfirmUpgrade(planName, planPrice)
                },
                colors = ButtonDefaults.buttonColors(containerColor = BizGreenPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("btn_confirm_stripe_payment")
            ) {
                Text("Confirmar e Assinar (R$ ${String.format("%.2f", planPrice)})", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cancelar")
            }
        },
        shape = RoundedCornerShape(22.dp)
    )
}
