package de.kaikarren.deamo

interface EventStore {

    fun store(conversationId: String, nativeEventAsString: String)

    fun store(conversationId: String, nativeEvents: List<String>)

    fun getNativeEvents(conversationId: String): List<String>

    fun getAllNativeEvents(): List<String>

}