package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val amount: Double,
    val type: String, // "INCOME", "EXPENSE", "TRANSFER"
    val category: String,
    val clientName: String = "",
    val dateMillis: Long = System.currentTimeMillis(),
    val notes: String = "",
    val paymentMethod: String = "Pix",
    val status: String = "COMPLETED" // "COMPLETED", "PENDING"
)

@Entity(tableName = "clients")
data class ClientEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val company: String = "",
    val email: String = "",
    val phone: String = "",
    val status: String = "Ativo", // "Ativo", "Pendente", "VIP"
    val totalDebt: Double = 0.0,
    val paidAmount: Double = 0.0,
    val notes: String = "",
    val createdAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "subscriptions")
data class SubscriptionEntity(
    @PrimaryKey val id: Int = 1,
    val planName: String = "Pro", // "Free", "Pro", "Business", "Enterprise"
    val status: String = "ACTIVE",
    val monthlyPrice: Double = 49.0,
    val renewalDateMillis: Long = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000),
    val aiAccessEnabled: Boolean = true,
    val crmAccessEnabled: Boolean = true,
    val unlimitedTransactions: Boolean = true,
    val teamMembersCount: Int = 3
)

@Entity(tableName = "audit_logs")
data class AuditLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestampMillis: Long = System.currentTimeMillis(),
    val userRole: String = "USER",
    val action: String,
    val details: String = ""
)

@Entity(tableName = "support_tickets")
data class SupportTicketEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subject: String,
    val status: String = "Aberto", // "Aberto", "Em Andamento", "Resolvido"
    val priority: String = "Média",
    val userEmail: String = "joaomendestyson@gmail.com",
    val dateMillis: Long = System.currentTimeMillis()
)
