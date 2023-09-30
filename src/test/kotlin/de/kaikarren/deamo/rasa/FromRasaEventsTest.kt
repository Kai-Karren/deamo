package de.kaikarren.deamo.rasa

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class FromRasaEventsTest {

    @Test
    fun loadEvent_givenCustomDataJSONPayload() {

        val eventAsString = """
            {"sender_id":"80","event":"bot","timestamp":1.6851172925229046E9,"metadata":{"model_id":"227a7a9477e544a8a91c3a740d5ed962"},"text":"Vielen Dank für Ihre Meldung. Wir haben Ihre Beschwerde erhalten und werden sie so schnell wie möglich prüfen und bearbeiten. Bitte haben Sie etwas Geduld, während wir uns um Ihr Anliegen kümmern.","data":{"custom":{"conversation_completed":true}}}
        """.trimIndent()

        val event = RasaEventConverter.loadEvent(eventAsString)

        assertEquals("80", event.getDialogId())

    }
}