package com.example.repository

import com.example.data.AuditLogDao
import com.example.data.AuditLogEntity
import com.example.data.ClientDao
import com.example.data.ClientEntity
import com.example.data.SubscriptionDao
import com.example.data.SubscriptionEntity
import com.example.data.SupportTicketDao
import com.example.data.SupportTicketEntity
import com.example.data.TransactionDao
import com.example.data.TransactionEntity
import kotlinx.coroutines.flow.Flow

class BizBalanceRepository(
    private val transactionDao: TransactionDao,
    private val clientDao: ClientDao,
    private val subscriptionDao: SubscriptionDao,
    private val auditLogDao: AuditLogDao,
    private val supportTicketDao: SupportTicketDao
) {
    val transactions: Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()
    val clients: Flow<List<ClientEntity>> = clientDao.getAllClients()
    val subscription: Flow<SubscriptionEntity?> = subscriptionDao.getSubscription()
    val auditLogs: Flow<List<AuditLogEntity>> = auditLogDao.getAllLogs()
    val supportTickets: Flow<List<SupportTicketEntity>> = supportTicketDao.getAllTickets()

    val totalIncome: Flow<Double?> = transactionDao.getTotalIncome()
    val totalExpense: Flow<Double?> = transactionDao.getTotalExpense()
    val totalClientDebt: Flow<Double?> = clientDao.getTotalClientDebt()

    suspend fun addTransaction(transaction: TransactionEntity): Long {
        val id = transactionDao.insertTransaction(transaction)
        auditLogDao.insertLog(
            AuditLogEntity(
                userRole = "USER",
                action = "Lançamento Criado",
                details = "${transaction.type}: ${transaction.title} - R$ ${String.format("%.2f", transaction.amount)}"
            )
        )
        return id
    }

    suspend fun updateTransaction(transaction: TransactionEntity) {
        transactionDao.updateTransaction(transaction)
        auditLogDao.insertLog(
            AuditLogEntity(
                userRole = "USER",
                action = "Lançamento Atualizado",
                details = "ID #${transaction.id}: ${transaction.title}"
            )
        )
    }

    suspend fun deleteTransaction(transaction: TransactionEntity) {
        transactionDao.deleteTransaction(transaction)
        auditLogDao.insertLog(
            AuditLogEntity(
                userRole = "USER",
                action = "Lançamento Excluído",
                details = "ID #${transaction.id}: ${transaction.title}"
            )
        )
    }

    suspend fun addClient(client: ClientEntity): Long {
        val id = clientDao.insertClient(client)
        auditLogDao.insertLog(
            AuditLogEntity(
                userRole = "USER",
                action = "Cliente Adicionado",
                details = "Cliente: ${client.name} (${client.company})"
            )
        )
        return id
    }

    suspend fun updateClient(client: ClientEntity) {
        clientDao.updateClient(client)
        auditLogDao.insertLog(
            AuditLogEntity(
                userRole = "USER",
                action = "Cliente Atualizado",
                details = "Cliente: ${client.name}"
            )
        )
    }

    suspend fun deleteClient(client: ClientEntity) {
        clientDao.deleteClient(client)
        auditLogDao.insertLog(
            AuditLogEntity(
                userRole = "USER",
                action = "Cliente Removido",
                details = "Cliente: ${client.name}"
            )
        )
    }

    suspend fun updateSubscription(planName: String, monthlyPrice: Double) {
        val currentSub = SubscriptionEntity(
            id = 1,
            planName = planName,
            status = "ACTIVE",
            monthlyPrice = monthlyPrice,
            renewalDateMillis = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000),
            aiAccessEnabled = true,
            crmAccessEnabled = planName != "Free",
            unlimitedTransactions = planName != "Free",
            teamMembersCount = when (planName) {
                "Enterprise" -> 20
                "Business" -> 10
                "Pro" -> 3
                else -> 1
            }
        )
        subscriptionDao.setSubscription(currentSub)
        auditLogDao.insertLog(
            AuditLogEntity(
                userRole = "USER",
                action = "Assinatura Alterada",
                details = "Plano atualizado para $planName (R$ ${String.format("%.2f", monthlyPrice)}/mês)"
            )
        )
    }

    suspend fun createSupportTicket(subject: String, userEmail: String): Long {
        val ticket = SupportTicketEntity(
            subject = subject,
            userEmail = userEmail,
            status = "Aberto",
            priority = "Alta",
            dateMillis = System.currentTimeMillis()
        )
        return supportTicketDao.insertTicket(ticket)
    }

    suspend fun updateTicketStatus(ticket: SupportTicketEntity, newStatus: String) {
        supportTicketDao.updateTicket(ticket.copy(status = newStatus))
        auditLogDao.insertLog(
            AuditLogEntity(
                userRole = "ADMIN",
                action = "Ticket de Suporte Atualizado",
                details = "Ticket #${ticket.id} modificado para $newStatus"
            )
        )
    }
}
