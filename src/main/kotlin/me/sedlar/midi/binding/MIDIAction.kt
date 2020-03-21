package me.sedlar.midi.binding

import javafx.scene.Node
import java.awt.Robot

internal const val DATA_BTN_IDLE = 0.toByte()

internal const val KNOB_MIN = 0.toByte()
internal const val KNOB_MAX = 127.toByte()

internal val robot = Robot()

abstract class MIDIAction(val name: String, val runTypes: Array<String> = arrayOf("Button", "Knob")) : Cloneable {

    var formData: String? = null

    abstract fun build(confirmer: Runnable): Pair<Node, Runnable>

    abstract fun execute(binding: MIDIBinding, btnData: Byte)

    fun copy(): MIDIAction {
        return clone() as MIDIAction
    }
}