package de.kaikarren.deamo

import de.kaikarren.conversations.data.Conversation
import java.time.Instant

interface ConversationStore {

    fun store(conversation: Conversation)

    fun getConversation(conversationId: String): Conversation

    fun hasConversation(conversationId: String): Boolean

    fun removeConversation(conversationId: String)

    fun getConversations(ids: List<String>): List<Conversation>

    fun getConversationIds(): List<String>

    fun getConversationIds(start: Instant, end: Instant): List<String>

}