package com.example.anothersmartvoicejournal.core.domain.model

data class Summary(
    val id: String,
    val entryId: String,
    val content: String,
    val bulletPoints: Int,
    val inputType: String, // "ARTICLE" or "CONVERSATION"
    val createdAt: Long,
    val confidence: Float?
) {
    val formattedDate: String
        get() = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
            .format(java.util.Date(createdAt))

    val bulletPointList: List<String>
        get() = content.split("\n")
            .filter { it.trim().isNotEmpty() }
            .map { it.trim() }
}
