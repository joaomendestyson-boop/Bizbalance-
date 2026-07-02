package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.AiMessage
import com.example.ai.GeminiAiService
import com.example.data.BizBalanceDatabase
import com.example.data.ClientEntity
import com.example.data.SubscriptionEntity
import com.example.data.SupportTicketEntity
import com.example.data.TransactionEntity
import com.example.repository.BizBalanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BizBalanceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BizBalanceRepository
    private val aiService = GeminiAiService()

    init {
        val database = BizBalanceDatabase.getDatabase(application, viewModelScope)
        repository = BizBalanceRepository(
            transactionDao = database.transactionDao(),
            clientDao = database.clientDao(),
            subscriptionDao = database.subscriptionDao(),
            auditLogDao = database.auditLogDao(),
            supportTicketDao = database.supportTicketDao()
        )
    }

    // Flows from database
    val transactions = repository.transactions.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val clients = repository.clients.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val subscription = repository.subscription.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), SubscriptionEntity()
    )

    val auditLogs = repository.auditLogs.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val supportTickets = repository.supportTickets.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val totalIncome = repository.totalIncome.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0
    )

    val totalExpense = repository.totalExpense.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0
    )

    val totalClientDebt = repository.totalClientDebt.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0
    )

    // Navigation & User Controls
    private val _showSplash = MutableStateFlow(false)
    val showSplash = _showSplash.asStateFlow()

    private val _selectedTab = MutableStateFlow(0) // 0: Home, 1: Finance, 2: CRM, 3: AI Assistant, 4: Reports, 5: Subscription, 6: Admin, 7: Brand Kit
    val selectedTab = _selectedTab.asStateFlow()

    private val _isAdminMode = MutableStateFlow(false)
    val isAdminMode = _isAdminMode.asStateFlow()

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode = _isDarkMode.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Todas")
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _selectedTypeFilter = MutableStateFlow("TODOS") // "TODOS", "INCOME", "EXPENSE"
    val selectedTypeFilter = _selectedTypeFilter.asStateFlow()

    // Dialog & Form States
    private val _showTransactionDialog = MutableStateFlow(false)
    val showTransactionDialog = _showTransactionDialog.asStateFlow()

    private val _editingTransaction = MutableStateFlow<TransactionEntity?>(null)
    val editingTransaction = _editingTransaction.asStateFlow()

    private val _showClientDialog = MutableStateFlow(false)
    val showClientDialog = _showClientDialog.asStateFlow()

    private val _editingClient = MutableStateFlow<ClientEntity?>(null)
    val editingClient = _editingClient.asStateFlow()

    private val _showStripeModal = MutableStateFlow(false)
    val showStripeModal = _showStripeModal.asStateFlow()

    private val _selectedPlanToUpgrade = MutableStateFlow("Pro")
    val selectedPlanToUpgrade = _selectedPlanToUpgrade.asStateFlow()

    private val _showSettingsSheet = MutableStateFlow(false)
    val showSettingsSheet = _showSettingsSheet.asStateFlow()

    // AI Chat & Assistant
    private val _aiMessages = MutableStateFlow<List<AiMessage>>(
        listOf(
            AiMessage(
                sender = "AI",
                text = "Olá! Sou o BizBalance AI, assistente financeiro inteligente fundado por João Gamba Mendes. Como posso ajudar a impulsionar seu negócio hoje?"
            )
        )
    )
    val aiMessages = _aiMessages.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading = _isAiLoading.asStateFlow()

    private val _latestAiInsight = MutableStateFlow("Otimize seus insumos operacionais para economizar até 12% este mês.")
    val latestAiInsight = _latestAiInsight.asStateFlow()

    // Filtered Transactions
    val filteredTransactions: StateFlow<List<TransactionEntity>> = combine(
        transactions, searchQuery, selectedCategory, selectedTypeFilter
    ) { txList, query, category, typeFilter ->
        txList.filter { tx ->
            val matchesQuery = query.isBlank() ||
                    tx.title.contains(query, ignoreCase = true) ||
                    tx.clientName.contains(query, ignoreCase = true) ||
                    tx.category.contains(query, ignoreCase = true)
            val matchesCategory = category == "Todas" || tx.category.equals(category, ignoreCase = true)
            val matchesType = typeFilter == "TODOS" || tx.type.equals(typeFilter, ignoreCase = true)
            matchesQuery && matchesCategory && matchesType
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        runInitialAiInsight()
    }

    fun openSplash() {
        _showSplash.value = true
    }

    fun dismissSplash() {
        _showSplash.value = false
    }

    fun openBrandKit() {
        _selectedTab.value = 7
        _showSplash.value = false
    }

    fun selectTab(tabIndex: Int) {
        _selectedTab.value = tabIndex
        _showSplash.value = false
    }

    fun toggleAdminMode() {
        _isAdminMode.value = !_isAdminMode.value
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCategoryFilter(category: String) {
        _selectedCategory.value = category
    }

    fun setTypeFilter(type: String) {
        _selectedTypeFilter.value = type
    }

    // Dialog Triggers
    fun openAddTransactionDialog() {
        _editingTransaction.value = null
        _showTransactionDialog.value = true
    }

    fun openEditTransactionDialog(tx: TransactionEntity) {
        _editingTransaction.value = tx
        _showTransactionDialog.value = true
    }

    fun closeTransactionDialog() {
        _showTransactionDialog.value = false
        _editingTransaction.value = null
    }

    fun openAddClientDialog() {
        _editingClient.value = null
        _showClientDialog.value = true
    }

    fun openEditClientDialog(client: ClientEntity) {
        _editingClient.value = client
        _showClientDialog.value = true
    }

    fun closeClientDialog() {
        _showClientDialog.value = false
        _editingClient.value = null
    }

    fun openStripeCheckout(planName: String) {
        _selectedPlanToUpgrade.value = planName
        _showStripeModal.value = true
    }

    fun closeStripeCheckout() {
        _showStripeModal.value = false
    }

    fun openSettingsSheet() {
        _showSettingsSheet.value = true
    }

    fun closeSettingsSheet() {
        _showSettingsSheet.value = false
    }

    // Actions
    fun saveTransaction(
        title: String,
        amount: Double,
        type: String,
        category: String,
        clientName: String,
        paymentMethod: String,
        notes: String
    ) {
        viewModelScope.launch {
            val currentEditing = _editingTransaction.value
            if (currentEditing != null) {
                repository.updateTransaction(
                    currentEditing.copy(
                        title = title,
                        amount = amount,
                        type = type,
                        category = category,
                        clientName = clientName,
                        paymentMethod = paymentMethod,
                        notes = notes
                    )
                )
            } else {
                repository.addTransaction(
                    TransactionEntity(
                        title = title,
                        amount = amount,
                        type = type,
                        category = category,
                        clientName = clientName,
                        paymentMethod = paymentMethod,
                        notes = notes,
                        dateMillis = System.currentTimeMillis()
                    )
                )
            }
            closeTransactionDialog()
            runInitialAiInsight()
        }
    }

    fun deleteTransaction(tx: TransactionEntity) {
        viewModelScope.launch {
            repository.deleteTransaction(tx)
            runInitialAiInsight()
        }
    }

    fun saveClient(
        name: String,
        company: String,
        email: String,
        phone: String,
        status: String,
        debt: Double,
        paid: Double,
        notes: String
    ) {
        viewModelScope.launch {
            val currentEditing = _editingClient.value
            if (currentEditing != null) {
                repository.updateClient(
                    currentEditing.copy(
                        name = name,
                        company = company,
                        email = email,
                        phone = phone,
                        status = status,
                        totalDebt = debt,
                        paidAmount = paid,
                        notes = notes
                    )
                )
            } else {
                repository.addClient(
                    ClientEntity(
                        name = name,
                        company = company,
                        email = email,
                        phone = phone,
                        status = status,
                        totalDebt = debt,
                        paidAmount = paid,
                        notes = notes
                    )
                )
            }
            closeClientDialog()
        }
    }

    fun deleteClient(client: ClientEntity) {
        viewModelScope.launch {
            repository.deleteClient(client)
        }
    }

    fun processStripeUpgrade(planName: String, price: Double) {
        viewModelScope.launch {
            repository.updateSubscription(planName, price)
            closeStripeCheckout()
        }
    }

    fun sendAiUserMessage(userText: String) {
        if (userText.isBlank()) return

        val currentList = _aiMessages.value.toMutableList()
        currentList.add(AiMessage(sender = "USER", text = userText))
        _aiMessages.value = currentList
        _isAiLoading.value = true

        viewModelScope.launch {
            val inc = totalIncome.value ?: 0.0
            val exp = totalExpense.value ?: 0.0
            val bal = inc - exp
            val txCount = transactions.value.size

            val response = aiService.generateBusinessInsight(
                totalIncome = inc,
                totalExpense = exp,
                balance = bal,
                recentTransactionsCount = txCount,
                userQuery = userText
            )

            val updatedList = _aiMessages.value.toMutableList()
            updatedList.add(AiMessage(sender = "AI", text = response))
            _aiMessages.value = updatedList
            _isAiLoading.value = false
        }
    }

    fun runInitialAiInsight() {
        viewModelScope.launch {
            val inc = totalIncome.value ?: 0.0
            val exp = totalExpense.value ?: 0.0
            val bal = inc - exp
            val txCount = transactions.value.size

            val result = aiService.generateBusinessInsight(
                totalIncome = inc,
                totalExpense = exp,
                balance = bal,
                recentTransactionsCount = txCount
            )
            _latestAiInsight.value = result.take(160) + if (result.length > 160) "..." else ""
        }
    }

    fun createSupportTicket(subject: String) {
        viewModelScope.launch {
            repository.createSupportTicket(subject, "joaomendestyson@gmail.com")
        }
    }

    fun updateTicketStatus(ticket: SupportTicketEntity, newStatus: String) {
        viewModelScope.launch {
            repository.updateTicketStatus(ticket, newStatus)
        }
    }
}
