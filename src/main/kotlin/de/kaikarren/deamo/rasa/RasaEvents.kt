package de.kaikarren.deamo.rasa

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import de.kaikarren.conversations.data.Entity
import de.kaikarren.conversations.data.Intent
import de.kaikarren.conversations.data.Slot
import de.kaikarren.deamo.ConvertibleEvent
import de.kaikarren.deamo.Event

/**
 * Provides data classes for Rasa Events.
 * Based on and my observations of additional information that is passed but not mentioned in the documentation.
 * https://rasa.com/docs/action-server/events/
 */

data class RasaEvent(
    @SerializedName("sender_id")
    val senderId: String = "",
    val event: String = "",
    val name: String = "",
) : ConvertibleEvent {
    override fun convert(): Event {
        return Event(
            conversationId = senderId,
            type = event,
            name = name,
        )
    }

    override fun getOrigin(): String {
        return "rasa"
    }

    override fun getDialogId(): String {
        return senderId
    }
}

data class UserEvent(
    @SerializedName("sender_id")
    val senderId: String = "",
    val event: String = "user",
    val text: String = "",
    @SerializedName("parse_data")
    val parseData: ParseData = ParseData()
) : ConvertibleEvent {
    override fun convert(): Event {

        val data = mutableMapOf<String, String>()

        data["intentRanking"] = Gson().toJson(parseData.intentRanking)
        data["intent"] = Gson().toJson(parseData.intent)
        data["entities"] = Gson().toJson(parseData.entities)

        return Event(
            conversationId = senderId,
            type = "user",
            data = data
        )
    }

    override fun getOrigin(): String {
        return "rasa"
    }

    override fun getDialogId(): String {
        return senderId
    }
}

data class BotEvent(
    @SerializedName("sender_id")
    val senderId: String = "",
    val event: String = "bot",
    val text: String = "",
    val data: Map<String, Any> = mapOf(),
    val metadata: Map<String, String> = mapOf(), // Rasa includes the executed action e.g. "utter_action": "utter_greet" here
): ConvertibleEvent {
    override fun convert(): Event {

        val dataMap = mutableMapOf<String, String>()

        dataMap.putAll(metadata)
        dataMap["text"] = text

        return Event(
            conversationId = senderId,
            type = "system",
            data = dataMap
        )
    }

    override fun getOrigin(): String {
        return "rasa"
    }

    override fun getDialogId(): String {
        return senderId
    }
}


data class SlotEvent(
    @SerializedName("sender_id")
    val senderId: String,
    val event: String = "",
    val name: String = "",
    val value: String = "",
): ConvertibleEvent {
    override fun convert(): Event {
        return Event(
            conversationId = senderId,
            type = event,
            name = "slot",
            data = mapOf(
                Pair("value", value)
            )
        )
    }

    override fun getOrigin(): String {
        return "rasa"
    }

    override fun getDialogId(): String {
        return senderId
    }

    fun toSlot(): Slot {
        return Slot(
            name = name,
            value = value,
        )
    }
}

data class ParseData(
    val intent: Intent = Intent(name = "default", 1.0, "not_specified"),
    val entities: List<Entity> = listOf(),
    @SerializedName("intent_ranking")
    val intentRanking: List<Intent> = listOf(),
)