package me.sedlar.midi.binding

import javafx.scene.Node
import java.awt.Robot
import kotlin.random.Random

internal const val DATA_BTN_IDLE = 0.toByte()

internal const val KNOB_MIN = 0.toByte()
internal const val KNOB_MAX = 127.toByte()

internal val robot = Robot()

abstract class MIDIAction(val name: String, val runTypes: Array<String> = arrayOf("Button", "Knob")) : Cloneable {

    var formData: String? = null
    var formArgs: Array<String> = emptyArray()

    var repeat = false

    internal var looping = false
    internal var released = false

    abstract fun build(confirmer: Runnable): Pair<Node, Runnable>

    abstract fun execute(binding: MIDIBinding, btnData: Byte)

    fun copy(): MIDIAction {
        return clone() as MIDIAction
    }

    internal fun doAndWaitForRelease(btnData: Byte, startTask: () -> Unit, finishTask: () -> Unit) {
        if (btnData != DATA_BTN_IDLE && !looping) {
            Thread {
                looping = true
                released = false
                startTask()
                while (!released) {
                    Thread.sleep(Random.nextLong(35, 45))
                }
                finishTask()
                looping = false
            }.start()
        } else if (btnData == DATA_BTN_IDLE) {
            looping = false
            released = true
        }
    }

    internal fun doWhileHeld(btnData: Byte, task: () -> Unit, finishTask: () -> Unit) {
        if (btnData != DATA_BTN_IDLE && !looping) {
            Thread {
                looping = true
                released = false
                while (!released) {
                    task()
                    Thread.sleep(Random.nextLong(35, 45))
                }
                finishTask()
                looping = false
            }.start()
        } else if (btnData == DATA_BTN_IDLE) {
            looping = false
            released = true
        }
    }
}