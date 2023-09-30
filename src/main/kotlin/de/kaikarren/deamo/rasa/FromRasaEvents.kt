package de.kaikarren.deamo.rasa

import de.kaikarren.conversations.data.Conversation
import de.kaikarren.conversations.data.Entity
import de.kaikarren.conversations.data.Intent
import de.kaikarren.conversations.data.Message
import de.kaikarren.deamo.ConvertibleEvent
import java.util.*

object FromRasaEvents {

    fun buildConversations(eventCollections: List<List<String>>): List<Conversation> {

        val dialogs = mutableListOf<Conversation>()

        for (eventsAsString in eventCollections) {

            val events = RasaEventConverter.loadEvents(eventsAsString)

            dialogs.add(buildConversationFromEvents(events))

        }

        return dialogs

    }


    fun buildConversationsFromEvents(eventGroups: List<List<ConvertibleEvent>>): List<Conversation> {

        val dialogs = mutableListOf<Conversation>()

        for (events in eventGroups) {

            dialogs.add(buildConversationFromEvents(events))

        }

        return dialogs

    }

    fun buildConversation(rasaEventsAsStrings: List<String>): Conversation {

        val rasaEvents = RasaEventConverter.loadEvents(rasaEventsAsStrings)

        return buildConversationFromEvents(rasaEvents)

    }

    fun buildConversationFromEvents(rasaEvents: List<ConvertibleEvent>): Conversation {

        val id = extractConversationId(rasaEvents)

        val conversation = Conversation(id)

        for (event in rasaEvents) {

            processEvent(event, conversation)

        }

        return conversation

    }

    private fun processEvent(event: ConvertibleEvent, conversation: Conversation) {

        when (event) {
            is BotEvent -> {

                val message = Message(
                    participant = "system",
                    text = event.text,
                )

                val utteranceName = event.metadata["utter_action"]

                if (utteranceName != null){
                    message.intents.add(Intent(utteranceName, 1.0, ""))
                }

                conversation.addMessage(message)


            }
            is UserEvent -> {

                val message = Message(
                    participant = "user",
                    text = event.text,
                    intents = mutableListOf(event.parseData.intent),
                    intentRanking = event.parseData.intentRanking as MutableList<Intent>,
                    entities = event.parseData.entities as MutableList<Entity>
                )

                conversation.addMessage(message)

            }
            is SlotEvent -> {

                conversation.messages.last().slots.add(event.toSlot())

            }
        }

    }

    private fun extractConversationId(rasaEvents: List<ConvertibleEvent>): String {

        for (event in rasaEvents){

            val id = event.getDialogId()

            if (id.isNotEmpty()) return id

        }

        return UUID.randomUUID().toString()

    }

}