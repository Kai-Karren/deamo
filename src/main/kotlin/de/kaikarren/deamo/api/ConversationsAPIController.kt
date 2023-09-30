package de.kaikarren.deamo.api

import de.kaikarren.conversations.data.Conversation
import de.kaikarren.deamo.ConversationStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class ConversationsAPIController {

    @Autowired
    lateinit var conversationStore: ConversationStore

    /**
     * Method to insert complete conversations with a single request.
     */
    @PostMapping(path = ["/conversations/{id}"])
    fun handleInsertConversation(@PathVariable id: String, @RequestBody conversation: Conversation): String{

        conversation.id = id

        conversationStore.store(conversation)

        return conversation.id

    }

    /**
     * Method to insert complete conversations with a single request.
     */
    @PostMapping(path = ["/conversations/insert"])
    fun handleInsertConversation(@RequestBody conversation: Conversation): String{

        conversationStore.store(conversation)

        return conversation.id

    }

    /**
     * Method to add a message to a conversation.
     * Designed to be used to sent updates to DEAMO while a conversation is still running.
     */
    @PostMapping(path = ["/conversations/{id}/messages/add"])
    fun handleUpdateConversation(@PathVariable id: String, @RequestBody updateRequest: UpdateConversationRequest): String{

        if(!conversationStore.hasConversation(updateRequest.id)){

            val dialog = Conversation(
                id = id,
                participants = mutableListOf(updateRequest.message.participant),
                messages = mutableListOf(updateRequest.message)
            )

            conversationStore.store(dialog)

        } else {
            val conversation = conversationStore.getConversation(updateRequest.id)

            conversation.addMessage(updateRequest.message)

            conversationStore.store(conversation)
        }

        return updateRequest.id

    }

    @GetMapping(path = ["/conversations/{id}"])
    fun handleGetConversation(@PathVariable id : String) : Conversation{

        return conversationStore.getConversation(id)

    }

    @DeleteMapping(path = ["/conversations/{id}"])
    fun handleRemoveConversation(@PathVariable id : String) : String{

        conversationStore.removeConversation(id)

        return id

    }

}