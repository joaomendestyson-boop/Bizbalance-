package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.BizBalanceBottomBar
import com.example.ui.components.TopHeader
import com.example.ui.dialogs.AddEditClientDialog
import com.example.ui.dialogs.AddEditTransactionDialog
import com.example.ui.dialogs.SettingsBottomSheet
import com.example.ui.dialogs.StripeCheckoutDialog
import com.example.ui.theme.BizBalanceTheme
import com.example.viewmodel.BizBalanceViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen(
    viewModel: BizBalanceViewModel = viewModel()
) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val showSplash by viewModel.showSplash.collectAsState()
    val isAdminMode by viewModel.isAdminMode.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()

    val subscription by viewModel.subscription.collectAsState()
    val totalIncome by viewModel.totalIncome.collectAsState()
    val totalExpense by viewModel.totalExpense.collectAsState()
    val totalClientDebt by viewModel.totalClientDebt.collectAsState()

    val transactions by viewModel.transactions.collectAsState()
    val filteredTransactions by viewModel.filteredTransactions.collectAsState()
    val clients by viewModel.clients.collectAsState()
    val auditLogs by viewModel.auditLogs.collectAsState()
    val supportTickets by viewModel.supportTickets.collectAsState()

    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedTypeFilter by viewModel.selectedTypeFilter.collectAsState()

    val showTransactionDialog by viewModel.showTransactionDialog.collectAsState()
    val editingTransaction by viewModel.editingTransaction.collectAsState()

    val showClientDialog by viewModel.showClientDialog.collectAsState()
    val editingClient by viewModel.editingClient.collectAsState()

    val showStripeModal by viewModel.showStripeModal.collectAsState()
    val selectedPlanToUpgrade by viewModel.selectedPlanToUpgrade.collectAsState()

    val showSettingsSheet by viewModel.showSettingsSheet.collectAsState()
    val aiMessages by viewModel.aiMessages.collectAsState()
    val isAiLoading by viewModel.isAiLoading.collectAsState()
    val latestAiInsight by viewModel.latestAiInsight.collectAsState()

    val currentSub = subscription ?: com.example.data.SubscriptionEntity()

    BizBalanceTheme(darkTheme = isDarkMode) {
        if (showSplash) {
            SplashScreen(onContinue = { viewModel.dismissSplash() })
        } else {
            Scaffold(
                topBar = {
                    TopHeader(
                        planName = currentSub.planName,
                        isAdminMode = isAdminMode,
                        onToggleAdmin = { viewModel.toggleAdminMode() },
                        onOpenSettings = { viewModel.openSettingsSheet() },
                        onOpenBrandKit = { viewModel.openBrandKit() }
                    )
                },
                bottomBar = {
                    BizBalanceBottomBar(
                        selectedIndex = selectedTab,
                        isAdminMode = isAdminMode,
                        onSelectTab = { viewModel.selectTab(it) }
                    )
                },
                containerColor = MaterialTheme.colorScheme.background
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    AnimatedContent(
                        targetState = selectedTab,
                        transitionSpec = { fadeIn() with fadeOut() },
                        label = "TabTransition"
                    ) { targetIndex ->
                        when (targetIndex) {
                            0 -> DashboardScreen(
                                totalIncome = totalIncome ?: 0.0,
                                totalExpense = totalExpense ?: 0.0,
                                transactions = transactions,
                                aiInsight = latestAiInsight,
                                onNavigateTab = { viewModel.selectTab(it) },
                                onOpenAddTransaction = { viewModel.openAddTransactionDialog() }
                            )
                            1 -> TransactionsScreen(
                                transactions = filteredTransactions,
                                searchQuery = searchQuery,
                                selectedCategory = selectedCategory,
                                selectedTypeFilter = selectedTypeFilter,
                                onSearchQueryChange = { viewModel.setSearchQuery(it) },
                                onCategoryChange = { viewModel.setCategoryFilter(it) },
                                onTypeFilterChange = { viewModel.setTypeFilter(it) },
                                onOpenAddTransaction = { viewModel.openAddTransactionDialog() },
                                onEditTransaction = { viewModel.openEditTransactionDialog(it) },
                                onDeleteTransaction = { viewModel.deleteTransaction(it) }
                            )
                            2 -> CrmScreen(
                                clients = clients,
                                totalClientDebt = totalClientDebt ?: 0.0,
                                onOpenAddClient = { viewModel.openAddClientDialog() },
                                onEditClient = { viewModel.openEditClientDialog(it) },
                                onDeleteClient = { viewModel.deleteClient(it) }
                            )
                            3 -> AiAssistantScreen(
                                aiMessages = aiMessages,
                                isAiLoading = isAiLoading,
                                onSendMessage = { viewModel.sendAiUserMessage(it) },
                                onRunAnalysis = { viewModel.runInitialAiInsight() }
                            )
                            4 -> ReportsScreen(
                                totalIncome = totalIncome ?: 0.0,
                                totalExpense = totalExpense ?: 0.0,
                                transactions = transactions
                            )
                            5 -> SubscriptionScreen(
                                currentSubscription = currentSub,
                                onOpenStripeCheckout = { viewModel.openStripeCheckout(it) }
                            )
                            6 -> AdminPanelScreen(
                                auditLogs = auditLogs,
                                supportTickets = supportTickets,
                                onUpdateTicketStatus = { ticket, newStatus -> viewModel.updateTicketStatus(ticket, newStatus) }
                            )
                            7 -> BrandKitScreen(
                                onShowSplash = { viewModel.openSplash() }
                            )
                            else -> DashboardScreen(
                                totalIncome = totalIncome ?: 0.0,
                                totalExpense = totalExpense ?: 0.0,
                                transactions = transactions,
                                aiInsight = latestAiInsight,
                                onNavigateTab = { viewModel.selectTab(it) },
                                onOpenAddTransaction = { viewModel.openAddTransactionDialog() }
                            )
                        }
                    }
                }

                // Dialog Overlays
                if (showTransactionDialog) {
                    AddEditTransactionDialog(
                        initialTransaction = editingTransaction,
                        onDismiss = { viewModel.closeTransactionDialog() },
                        onSave = { title, amount, type, category, clientName, paymentMethod, notes ->
                            viewModel.saveTransaction(title, amount, type, category, clientName, paymentMethod, notes)
                        }
                    )
                }

                if (showClientDialog) {
                    AddEditClientDialog(
                        initialClient = editingClient,
                        onDismiss = { viewModel.closeClientDialog() },
                        onSave = { name, company, email, phone, status, debt, paid, notes ->
                            viewModel.saveClient(name, company, email, phone, status, debt, paid, notes)
                        }
                    )
                }

                if (showStripeModal) {
                    StripeCheckoutDialog(
                        planName = selectedPlanToUpgrade,
                        onDismiss = { viewModel.closeStripeCheckout() },
                        onConfirmUpgrade = { planName, price ->
                            viewModel.processStripeUpgrade(planName, price)
                        }
                    )
                }

                if (showSettingsSheet) {
                    SettingsBottomSheet(
                        isDarkMode = isDarkMode,
                        onToggleDarkMode = { viewModel.toggleDarkMode() },
                        onCreateSupportTicket = { viewModel.createSupportTicket(it) },
                        onDismiss = { viewModel.closeSettingsSheet() }
                    )
                }
            }
        }
    }
}
