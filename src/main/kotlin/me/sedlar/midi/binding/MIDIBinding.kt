package me.sedlar.midi.binding

import me.sedlar.midi.binding.actions.*

internal val ACTIONS = arrayOf(
    KeyAction(),
    KeyRepeatAction(),
    MultiKeyAction(),
    ProgramAction(),
    CommandAction(),
    ButtonAction(),
    KnobAction()
)

open class MIDIBinding(
    val btn: Byte,
    val trigger: String = "!0",
    val type: String = "button",
    val output: String,
    val data: String
) {

    val action: MIDIAction? = ACTIONS.find { it.name == output }?.copy()

    fun execute(btnData: Byte) {
        action?.execute(this, btnData)
    }
}