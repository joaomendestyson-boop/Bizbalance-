package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        TransactionEntity::class,
        ClientEntity::class,
        SubscriptionEntity::class,
        AuditLogEntity::class,
        SupportTicketEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BizBalanceDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun clientDao(): ClientDao
    abstract fun subscriptionDao(): SubscriptionDao
    abstract fun auditLogDao(): AuditLogDao
    abstract fun supportTicketDao(): SupportTicketDao

    companion object {
        @Volatile
        private var INSTANCE: BizBalanceDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): BizBalanceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BizBalanceDatabase::class.java,
                    "bizbalance_database"
                )
                .addCallback(BizBalanceDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class BizBalanceDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database)
                }
            }
        }

        suspend fun populateDatabase(db: BizBalanceDatabase) {
            val transactionDao = db.transactionDao()
            val clientDao = db.clientDao()
            val subscriptionDao = db.subscriptionDao()
            val auditLogDao = db.auditLogDao()
            val supportTicketDao = db.supportTicketDao()

            val now = System.currentTimeMillis()
            val day = 24 * 60 * 60 * 1000L

            // Initial Subscription
            subscriptionDao.setSubscription(
                SubscriptionEntity(
                    id = 1,
                    planName = "Pro",
                    status = "ACTIVE",
                    monthlyPrice = 49.0,
                    renewalDateMillis = now + (28 * day),
                    aiAccessEnabled = true,
                    crmAccessEnabled = true,
                    unlimitedTransactions = true,
                    teamMembersCount = 3
                )
            )

            // Initial Sample Transactions
            transactionDao.insertTransaction(
                TransactionEntity(
                    title = "Venda de Consultoria #0842",
                    amount = 3850.00,
                    type = "INCOME",
                    category = "Serviços",
                    clientName = "Silva & Filhos Ltda",
                    dateMillis = now - (1 * day),
                    notes = "Pagamento via Pix aprovado",
                    paymentMethod = "Pix",
                    status = "COMPLETED"
                )
            )
            transactionDao.insertTransaction(
                TransactionEntity(
                    title = "Mercado Central - Insumos",
                    amount = 420.00,
                    type = "EXPENSE",
                    category = "Insumos",
                    clientName = "",
                    dateMillis = now - (2 * day),
                    notes = "Compra de materiais de escritório e estoque",
                    paymentMethod = "Cartão",
                    status = "COMPLETED"
                )
            )
            transactionDao.insertTransaction(
                TransactionEntity(
                    title = "Contrato Mensal de Software SaaS",
                    amount = 1250.00,
                    type = "INCOME",
                    category = "Vendas",
                    clientName = "TechSul Soluções",
                    dateMillis = now - (3 * day),
                    notes = "Assinatura trimestral",
                    paymentMethod = "Boleto",
                    status = "COMPLETED"
                )
            )
            transactionDao.insertTransaction(
                TransactionEntity(
                    title = "Servidor Cloud & Firebase Hosting",
                    amount = 189.90,
                    type = "EXPENSE",
                    category = "Software",
                    clientName = "",
                    dateMillis = now - (4 * day),
                    notes = "Infraestrutura SaaS",
                    paymentMethod = "Cartão",
                    status = "COMPLETED"
                )
            )
            transactionDao.insertTransaction(
                TransactionEntity(
                    title = "Anúncios Meta Ads & Google Ads",
                    amount = 600.00,
                    type = "EXPENSE",
                    category = "Marketing",
                    clientName = "",
                    dateMillis = now - (5 * day),
                    notes = "Campanha de aquisição de clientes",
                    paymentMethod = "Cartão",
                    status = "COMPLETED"
                )
            )
            transactionDao.insertTransaction(
                TransactionEntity(
                    title = "Projeto Desenvolvido #102",
                    amount = 5400.00,
                    type = "INCOME",
                    category = "Serviços",
                    clientName = "Mercado & Cia",
                    dateMillis = now - (6 * day),
                    notes = "Segunda parcela de projeto web",
                    paymentMethod = "Pix",
                    status = "COMPLETED"
                )
            )

            // Initial Clients
            clientDao.insertClient(
                ClientEntity(
                    name = "Silva & Filhos Ltda",
                    company = "Grupo Silva",
                    email = "contato@silvafilhos.com.br",
                    phone = "(11) 98844-2211",
                    status = "VIP",
                    totalDebt = 0.0,
                    paidAmount = 14500.0,
                    notes = "Cliente recorrente há 2 anos"
                )
            )
            clientDao.insertClient(
                ClientEntity(
                    name = "TechSul Soluções",
                    company = "TechSul Softwares",
                    email = "financeiro@techsul.com",
                    phone = "(41) 99122-3344",
                    status = "Ativo",
                    totalDebt = 1250.0,
                    paidAmount = 8900.0,
                    notes = "Boleto pendente com vencimento para dia 15"
                )
            )
            clientDao.insertClient(
                ClientEntity(
                    name = "Mercado & Cia",
                    company = "Mercado & Cia Distribuidora",
                    email = "atendimento@mercadocia.com",
                    phone = "(21) 97766-5544",
                    status = "Pendente",
                    totalDebt = 2100.0,
                    paidAmount = 5400.0,
                    notes = "Solicitou nota fiscal atualizada"
                )
            )

            // Initial Audit Logs
            auditLogDao.insertLog(
                AuditLogEntity(
                    userRole = "ADMIN",
                    action = "Inicialização do Sistema BizBalance",
                    details = "SaaS iniciado por João Gamba Mendes - Operação Pro Ativa"
                )
            )
            auditLogDao.insertLog(
                AuditLogEntity(
                    userRole = "USER",
                    action = "Login do Usuário",
                    details = "Acesso efetuado via Firebase Authentication"
                )
            )

            // Initial Support Ticket
            supportTicketDao.insertTicket(
                SupportTicketEntity(
                    subject = "Dúvida sobre integração de boleto Stripe",
                    status = "Resolvido",
                    priority = "Média",
                    userEmail = "joaomendestyson@gmail.com"
                )
            )
        }
    }
}
