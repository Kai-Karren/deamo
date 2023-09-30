package de.kaikarren.deamo

object EventConverter {

    fun convertEvents(events: List<ConvertibleEvent>): List<Event> {

        val convertedEvents = mutableListOf<Event>()

        for (event in events){
            convertedEvents.add(event.convert())
        }

        return convertedEvents

    }

}