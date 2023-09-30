package de.kaikarren.deamo.api

import de.kaikarren.conversations.data.Message
import java.util.*

data class UpdateConversationRequest(
    val id: String = UUID.randomUUID().toString(),
    val message : Message,
)
