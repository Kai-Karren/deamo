package de.kaikarren.deamo.rasa

import com.google.gson.Gson
import de.kaikarren.deamo.ConvertibleEvent
import de.kaikarren.deamo.Event

/**
 * Object that allows to convert Rasa Events into internal Events
 */
object RasaEventConverter {

    fun loadEvents(eventAsStringList: List<String>): List<ConvertibleEvent> {

        val events = mutableListOf<ConvertibleEvent>()

        for (eventAsString in eventAsStringList) {
            events.add(loadEvent(eventAsString))
        }

        return events

    }

    fun loadEvent(eventAsString: String): ConvertibleEvent {

        val event = Gson().fromJson(eventAsString, RasaEvent::class.java)

        when (event.event) {
            "user" -> {

                return Gson().fromJson(eventAsString, UserEvent::class.java)

            }
            "bot" -> {

                return Gson().fromJson(eventAsString, BotEvent::class.java)

            }
            "slot" -> {

                return Gson().fromJson(eventAsString, SlotEvent::class.java)

            }

            else -> {
                return event
            }


        }

    }

    fun loadAndConvertEvent(eventAsString: String): Event {

        val event = Gson().fromJson(eventAsString, RasaEvent::class.java)

        when (event.event) {
            "user" -> {

                val userEvent = Gson().fromJson(eventAsString, UserEvent::class.java)

                println("${userEvent.event}: ${userEvent.text}")

                return userEvent.convert()

            }
            "bot" -> {

                val botEvent = Gson().fromJson(eventAsString, BotEvent::class.java)

                println("${botEvent.event}: ${botEvent.text}")

                return botEvent.convert()

            }
            "slot" -> {

                val slotEvent = Gson().fromJson(eventAsString, SlotEvent::class.java)

                println("${slotEvent.event}: ${slotEvent.name} ${slotEvent.value}")

                return slotEvent.convert()

            }

            else -> {
                return event.convert()
            }


        }

    }

}