package de.kaikarren.deamo

import java.util.concurrent.ConcurrentHashMap

class InMemoryEventStore: EventStore {

    private val events = ConcurrentHashMap<String, MutableList<String>>()

    override fun store(conversationId: String, nativeEventAsString: String) {

        if (events.containsKey(conversationId)){

            val eventList = events[conversationId]

            if (!eventList.isNullOrEmpty()) {
                eventList.add(nativeEventAsString)
            } else {
                events[conversationId] = mutableListOf(nativeEventAsString)
            }

        } else {
            events[conversationId] = mutableListOf(nativeEventAsString)
        }
    }

    override fun store(conversationId: String, nativeEvents: List<String>) {

        for (event in nativeEvents) {
            store(conversationId, event)
        }

    }

    override fun getNativeEvents(conversationId: String): List<String> {

        if (events.containsKey(conversationId)){
            return events[conversationId]?.toList() ?: mutableListOf()
        }

        return mutableListOf()
    }

    override fun getAllNativeEvents(): List<String> {

        val nativeEventList = mutableListOf<String>()

        for (dialogId in events.keys()) {
            events[dialogId]?.let { nativeEventList.addAll(it) }
        }

        return nativeEventList

    }

}