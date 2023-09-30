package de.kaikarren.deamo.rasa

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.kaikarren.deamo.ConversationStore
import de.kaikarren.deamo.EventStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class RasaEndpointController {

    @Autowired
    lateinit var eventStore: EventStore

    @Autowired
    lateinit var conversationStore: ConversationStore

    @PostMapping(path = ["/rasa/event"])
    fun handleRasaEvent(@RequestBody body: String): String {

        val rasaEvent = Gson().fromJson(body, RasaEvent::class.java)

        eventStore.store(rasaEvent.senderId, body)

        return "{}"
    }

    @PostMapping(path = ["/conversations/{conversationId}/rasa/events"])
    fun handleAddRasaEventsForConversationId(@PathVariable conversationId: String, @RequestBody body: String): String {

        val typeToken = object : TypeToken<List<String>>() {}.type

        val rasaEventsAsString = Gson().fromJson<List<String>>(body, typeToken)

        eventStore.store(conversationId, rasaEventsAsString)

        return "{}"
    }

    @PostMapping(path = ["/rasa/events"])
    fun handleAddRasaEvents(@RequestBody body: String): String {

        val typeToken = object : TypeToken<List<RasaEvent>>() {}.type

        val rasaEventsAsAny = Gson().fromJson(body, List::class.java)

        val rasaEvents = Gson().fromJson<List<RasaEvent>>(body, typeToken)

        for (i in rasaEventsAsAny.indices) {

            val event = rasaEvents[i]

            eventStore.store(event.senderId, Gson().toJson(rasaEventsAsAny[i]))

        }

        return "{}"
    }

    @GetMapping(path = ["/rasa/events"], produces = ["application/json"])
    fun handleGetAllRasaEvents(): String {

        return eventStore.getAllNativeEvents().toString()

    }

    /**
     * Method to manually trigger the processing of the stored events with Deamo.
     * Could later be run from time to time automatically.
     */
    @GetMapping(path = ["/rasa/events/process/{id}"], produces = ["application/json"])
    fun handleProcessStoredEventsForId(@PathVariable id: String): String {

        val events = eventStore.getNativeEvents(id)

        val conversation = FromRasaEvents.buildConversation(events)

        conversationStore.store(conversation)

        return Gson().toJson(conversation)

    }

    @GetMapping(path = ["/rasa/events/{id}"], produces = ["application/json"])
    fun handleGetEventsForId(@PathVariable id: String): String {

        val events = eventStore.getNativeEvents(id)

        return events.toString()

    }


}