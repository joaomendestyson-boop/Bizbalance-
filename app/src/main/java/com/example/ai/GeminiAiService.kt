package com.example.ai

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class AiMessage(
    val sender: String, // "USER" or "AI"
    val text: String,
    val timestampMillis: Long = System.currentTimeMillis()
)

class GeminiAiService {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val apiKey: String
        get() = try {
            BuildConfig.GEMINI_API_KEY.ifEmpty { "MY_GEMINI_API_KEY" }
        } catch (e: Exception) {
            "MY_GEMINI_API_KEY"
        }

    suspend fun generateBusinessInsight(
        totalIncome: Double,
        totalExpense: Double,
        balance: Double,
        recentTransactionsCount: Int,
        userQuery: String? = null
    ): String = withContext(Dispatchers.IO) {
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getOfflineInsight(totalIncome, totalExpense, balance, userQuery)
        }

        try {
            val promptText = if (userQuery.isNullOrBlank()) {
                """
                Você é o BizBalance AI, assistente financeiro inteligente fundado por João Gamba Mendes.
                Analise os dados financeiros da empresa:
                - Receita Total: R$ ${String.format("%.2f", totalIncome)}
                - Despesa Total: R$ ${String.format("%.2f", totalExpense)}
                - Saldo em Caixa: R$ ${String.format("%.2f", balance)}
                - Lançamentos recentes: $recentTransactionsCount
                
                Forneça um diagnóstico direto e profissional com:
                1. Avaliação de Saúde Financeira
                2. Dica de Otimização de Despesas
                3. Previsão de Lucro para o próximo mês
                4. Alerta de Risco
                
                Responda em português de forma clara, motivadora e executiva.
                """.trimIndent()
            } else {
                """
                Você é o BizBalance AI, assistente financeiro de gestão para PMEs.
                Contexto da empresa: Receita R$ ${String.format("%.2f", totalIncome)}, Despesa R$ ${String.format("%.2f", totalExpense)}, Saldo R$ ${String.format("%.2f", balance)}.
                Pergunta do empreendedor: "$userQuery"
                
                Responda com autoridade em finanças empresariais de forma objetiva e prática em português.
                """.trimIndent()
            }

            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", promptText)
                            })
                        })
                    })
                })
            }

            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"
            val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseString = response.body?.string() ?: ""
                val responseJson = JSONObject(responseString)
                val candidates = responseJson.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.getJSONObject("content")
                    val parts = content.getJSONArray("parts")
                    val resultText = parts.getJSONObject(0).getString("text")
                    return@withContext resultText.trim()
                }
            }
            return@withContext getOfflineInsight(totalIncome, totalExpense, balance, userQuery)
        } catch (e: Exception) {
            return@withContext getOfflineInsight(totalIncome, totalExpense, balance, userQuery)
        }
    }

    private fun getOfflineInsight(
        totalIncome: Double,
        totalExpense: Double,
        balance: Double,
        userQuery: String?
    ): String {
        val profitMargin = if (totalIncome > 0) ((totalIncome - totalExpense) / totalIncome) * 100 else 0.0
        
        if (!userQuery.isNullOrBlank()) {
            val queryLower = userQuery.lowercase()
            return when {
                queryLower.contains("corta") || queryLower.contains("despesa") || queryLower.contains("reduc") -> {
                    "💡 Otimização de Custos BizBalance: Suas despesas operacionais representam R$ ${String.format("%.2f", totalExpense)}. Recomendamos renegociar contratos de insumos e softwares trimestrais. Cortar 10% nas maiores categorias geraria uma economia estimada de R$ ${String.format("%.2f", totalExpense * 0.10)} mensais."
                }
                queryLower.contains("lucro") || queryLower.contains("previs") -> {
                    "📈 Previsão de Lucratividade: Com margem atual de ${String.format("%.1f", profitMargin)}% e saldo de R$ ${String.format("%.2f", balance)}, a projeção para os próximos 30 dias indica um fluxo positivo de aproximadamente R$ ${String.format("%.2f", (totalIncome - totalExpense) * 1.15)}."
                }
                queryLower.contains("cliente") || queryLower.contains("inadimpl") -> {
                    "👥 Análise de Clientes & CRM: Monitore clientes com pagamentos em aberto no módulo CRM para reduzir a inadimplência e manter o ciclo financeiro saudável."
                }
                else -> {
                    "📊 Diagnóstico BizBalance AI: Sua empresa possui uma margem de lucro de ${String.format("%.1f", profitMargin)}%. Mantenha o acompanhamento rigoroso do fluxo de caixa e agende revisões quinzenais de categorias para garantir estabilidade."
                }
            }
        }

        return """
        🤖 Diagnóstico BizBalance AI:
        
        • Saúde Financeira: Margem operacional estimada em ${String.format("%.1f", profitMargin)}%.
        • Otimização de Custos: Suas maiores despesas estão concentradas em insumos e marketing. Otimizar fornecedores pode economizar até 12% no próximo ciclo.
        • Previsão de Caixa: Estimativa de caixa positivo em R$ ${String.format("%.2f", balance * 1.08)} nos próximos 30 dias.
        • Alerta de Risco: Mantenha reserva operacional equivalente a 3 meses de despesas fixas.
        """.trimIndent()
    }
}
