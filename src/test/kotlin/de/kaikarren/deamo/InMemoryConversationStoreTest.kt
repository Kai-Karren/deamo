package de.kaikarren.deamo

import de.kaikarren.conversations.data.Conversation
import de.kaikarren.conversations.data.Label
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.time.Instant
import java.util.UUID

class InMemoryConversationStoreTest {

    @Test
    fun testGetConversation() {

        val conversationStore = InMemoryConversationStore()

        val id = UUID.randomUUID().toString()

        val conversation = Conversation(
            id = id,
            timestamp = Instant.now().toString(),
            labels = mutableListOf(Label(name = "test_label", confidence = 1.0, "unit_test"))
        )

        conversationStore.store(conversation)

        val fetchedConversation = conversationStore.getConversation(id)

        assertEquals("test_label", fetchedConversation.labels[0].name)

    }

    @Test
    fun testHasConversation() {

        val conversationStore = InMemoryConversationStore()

        val id = UUID.randomUUID().toString()

        val conversation = Conversation(
            id = id,
            timestamp = Instant.now().toString(),
            labels = mutableListOf(Label(name = "test_label", confidence = 1.0, "unit_test"))
        )

        conversationStore.store(conversation)

        assertTrue(conversationStore.hasConversation(id))

    }

    @Test
    fun testRemoveConversation() {

        val conversationStore = InMemoryConversationStore()

        val id = UUID.randomUUID().toString()

        val conversation = Conversation(
            id = id,
            timestamp = Instant.now().toString(),
            labels = mutableListOf(Label(name = "test_label", confidence = 1.0, "unit_test"))
        )

        conversationStore.store(conversation)

        assertTrue(conversationStore.hasConversation(id))

        conversationStore.removeConversation(id)

        assertFalse(conversationStore.hasConversation(id))

    }

    @Test
    fun testGetConversations() {

        val conversationStore = InMemoryConversationStore()

        val id = UUID.randomUUID().toString()

        val conversation = Conversation(
            id = id,
            timestamp = Instant.now().toString(),
            labels = mutableListOf(Label(name = "test_label", confidence = 1.0, "unit_test"))
        )

        val secondId = UUID.randomUUID().toString()

        val secondConversation = Conversation(
            id = secondId,
            timestamp = Instant.now().toString(),
            labels = mutableListOf(Label(name = "another_label", confidence = 1.0, "unit_test"))
        )

        conversationStore.store(conversation)
        conversationStore.store(secondConversation)

        val fetchConversations = conversationStore.getConversations(listOf(id, secondId))

        assertEquals(2, fetchConversations.size)

    }

    @Test
    fun testGetConversationIds() {

        val conversationStore = InMemoryConversationStore()

        val id = UUID.randomUUID().toString()

        val conversation = Conversation(
            id = id,
            timestamp = Instant.now().toString(),
            labels = mutableListOf(Label(name = "test_label", confidence = 1.0, "unit_test"))
        )

        val secondId = UUID.randomUUID().toString()

        val secondConversation = Conversation(
            id = secondId,
            timestamp = Instant.now().toString(),
            labels = mutableListOf(Label(name = "another_label", confidence = 1.0, "unit_test"))
        )

        conversationStore.store(conversation)
        conversationStore.store(secondConversation)

        val ids = conversationStore.getConversationIds()

        assertEquals(2, ids.size)

        assertTrue(ids[0] == id || ids[0] == secondId)
        assertTrue(ids[1] == id || ids[1] == secondId)

    }

    @Test
    fun testGetConversationIdsInTimeRange() {

        val conversationStore = InMemoryConversationStore()

        val id = UUID.randomUUID().toString()

        val conversation = Conversation(
            id = id,
            timestamp = Instant.now().toString(),
            labels = mutableListOf(Label(name = "test_label", confidence = 1.0, "unit_test"))
        )

        val secondId = UUID.randomUUID().toString()

        val secondConversation = Conversation(
            id = secondId,
            timestamp = "2023-09-30T15:30:00+00:00",
            labels = mutableListOf(Label(name = "another_label", confidence = 1.0, "unit_test"))
        )

        conversationStore.store(conversation)
        conversationStore.store(secondConversation)

        val ids = conversationStore.getConversationIds(
            Instant.parse("2023-09-30T15:00:00+00:00"),
            Instant.parse("2023-09-30T16:00:00+00:00")
        )

        assertEquals(1, ids.size)

        assertEquals(secondId, ids[0])

    }

}