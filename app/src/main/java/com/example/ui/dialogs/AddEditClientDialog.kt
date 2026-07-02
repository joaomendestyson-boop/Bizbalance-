package com.example.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ClientEntity
import com.example.ui.theme.BizGreenPrimary
import com.example.ui.theme.BizGreenSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditClientDialog(
    initialClient: ClientEntity?,
    onDismiss: () -> Unit,
    onSave: (
        name: String,
        company: String,
        email: String,
        phone: String,
        status: String,
        debt: Double,
        paid: Double,
        notes: String
    ) -> Unit
) {
    var name by remember { mutableStateOf(initialClient?.name ?: "") }
    var company by remember { mutableStateOf(initialClient?.company ?: "") }
    var email by remember { mutableStateOf(initialClient?.email ?: "") }
    var phone by remember { mutableStateOf(initialClient?.phone ?: "") }
    var status by remember { mutableStateOf(initialClient?.status ?: "Ativo") }
    var debtText by remember { mutableStateOf(initialClient?.totalDebt?.toString() ?: "0.0") }
    var paidText by remember { mutableStateOf(initialClient?.paidAmount?.toString() ?: "0.0") }
    var notes by remember { mutableStateOf(initialClient?.notes ?: "") }

    var statusExpanded by remember { mutableStateOf(false) }
    val statusOptions = listOf("Ativo", "Pendente", "VIP")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (initialClient == null) "Cadastrar Novo Cliente" else "Editar Cliente",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = BizGreenSecondary,
                    fontSize = 18.sp
                )
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome do Cliente *") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_client_name"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = company,
                    onValueChange = { company = it },
                    label = { Text("Empresa / Razão Social") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_client_company"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("E-mail") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("dialog_client_email"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Telefone / WhatsApp") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("dialog_client_phone"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }

                ExposedDropdownMenuBox(
                    expanded = statusExpanded,
                    onExpandedChange = { statusExpanded = !statusExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = status,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Status do Cliente") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .testTag("dialog_client_status"),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = statusExpanded,
                        onDismissRequest = { statusExpanded = false }
                    ) {
                        statusOptions.forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt) },
                                onClick = {
                                    status = opt
                                    statusExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = debtText,
                    onValueChange = { debtText = it },
                    label = { Text("Dívida Pendente (R$)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_client_debt"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val debtVal = debtText.replace(",", ".").toDoubleOrNull() ?: 0.0
                    val paidVal = paidText.replace(",", ".").toDoubleOrNull() ?: 0.0
                    if (name.isNotBlank()) {
                        onSave(name, company, email, phone, status, debtVal, paidVal, notes)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BizGreenPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("btn_save_client")
            ) {
                Text("Salvar Cliente", fontWeight = FontWeight.Bold)
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
