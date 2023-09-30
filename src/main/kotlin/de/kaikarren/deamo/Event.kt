package de.kaikarren.deamo

data class Event(
    val conversationId: String = "",
    val type: String = "",
    val name: String = "",
    val data: Map<String, String> = mutableMapOf()
)
