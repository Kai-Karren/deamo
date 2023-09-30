package de.kaikarren.deamo

import de.kaikarren.conversations.data.Conversation
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

class InMemoryConversationStore: ConversationStore {

    private val conversations = ConcurrentHashMap<String, Conversation>()

    private val timeIndex = ConcurrentHashMap<String, Instant>()

    override fun store(conversation: Conversation) {
        conversations[conversation.id] = conversation
        timeIndex[conversation.id] = Instant.parse(conversation.timestamp)
    }

    override fun getConversation(conversationId: String): Conversation {
        return conversations[conversationId] ?: Conversation(participants = mutableListOf(), messages = mutableListOf())
    }

    override fun hasConversation(conversationId: String): Boolean {
        return conversations.containsKey(conversationId)
    }

    override fun removeConversation(conversationId: String) {
        conversations.remove(conversationId)
    }

    override fun getConversations(ids: List<String>): List<Conversation> {

        val dialogs = mutableListOf<Conversation>()

        for (id in ids) {

            dialogs.add(getConversation(id))

        }

        return dialogs

    }

    override fun getConversationIds(): List<String> {
        return conversations.keys().toList()
    }

    override fun getConversationIds(start: Instant, end: Instant): List<String> {

        val ids = mutableListOf<String>()

        for (entry in timeIndex){

            val id = entry.component1()
            val timestamp = entry.component2()

            if (timestamp.isAfter(start) && timestamp.isBefore(end)){
                ids.add(id)
            }

        }

        return ids

    }

}