package de.kaikarren.deamo

interface ConvertibleEvent {

    fun convert(): Event

    fun getOrigin(): String

    fun getDialogId(): String

}