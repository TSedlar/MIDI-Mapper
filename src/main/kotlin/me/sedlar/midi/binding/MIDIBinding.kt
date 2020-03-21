package me.sedlar.midi.binding

import me.sedlar.ext.mouseClick
import me.sedlar.util.Keyboard
import java.awt.*
import java.awt.event.KeyEvent
import java.io.File
import kotlin.random.Random

private const val DATA_BTN_IDLE = 0.toByte()

private const val KNOB_MIN = 0.toByte()
private const val KNOB_MAX = 127.toByte()

private val robot = Robot()

private val size = GraphicsEnvironment.getLocalGraphicsEnvironment().maximumWindowBounds
private val center = Point(size.width / 2, size.height / 2)

private var x = center.x
private var y = center.y
private var offX = 0
private var offY = 0

open class MIDIBinding(
    val btn: Byte,
    val trigger: String = "!0",
    val type: String = "button",
    val output: String,
    val data: String,
    val args: String? = null
) {

    private var looping = false
    private var released = false

    fun execute(btnData: Byte) {
        when (output) {
            "Repeat key",
            "Key" -> {
                val repeater = output == "Repeat key"
                val keys = data.split(" + ")
                val keyList = ArrayList<Int>()
                for (key in keys) {
                    if (key.length == 1) {
                        keyList.add(Keyboard.getKeyStroke(key[0]).keyCode)
                    } else {
                        when (key) {
                            "Space" -> keyList.add(KeyEvent.VK_SPACE)
                            "Ctrl" -> keyList.add(KeyEvent.VK_CONTROL)
                            "Alt" -> keyList.add(KeyEvent.VK_ALT)
                            "Shift" -> keyList.add(KeyEvent.VK_SHIFT)
                            "Tab" -> keyList.add(KeyEvent.VK_TAB)
                        }
                    }
                }
                if (repeater) {
                    if (btnData != DATA_BTN_IDLE && !looping) {
                        Thread {
                            looping = true
                            released = false
                            while (!released) {
                                for (key in keyList) {
                                    robot.keyPress(key)
                                }
                                Thread.sleep(Random.nextLong(35, 45))
                            }
                            for (key in keyList) {
                                robot.keyRelease(key)
                            }
                            looping = false
                        }.start()
                    } else if (btnData == DATA_BTN_IDLE) {
                        looping = false
                        released = true
                    }
                } else {
                    for (key in keyList) {
                        robot.keyPress(key)
                    }
                    for (key in keyList.reversed()) {
                        robot.keyRelease(key)
                    }
                }
            }
            "Multikey" -> {
                Keyboard.type(data)
            }
            "Program" -> {
                // TODO: apply args, check if file is .jar -> use java, .py -> use python, etc.
                // TODO: until then, just link to a .bat or .sh
                Desktop.getDesktop().open(File(data))
            }
            "Action" -> {
                when (data) {
                    "Left click" -> {
                        robot.mouseClick(true)
                    }
                    "Right click" -> {
                        robot.mouseClick(false)
                    }
                    "Mouse X" -> {
                        x = center.x + when {
                            btnData < KNOB_MAX / 2 -> {
                                // left
                                val ratio = 1.0 - (btnData.toDouble() / (KNOB_MAX / 2).toDouble())
                                -((size.width / 2) * ratio).toInt()
                            }
                            btnData > KNOB_MAX / 2 -> {
                                // right
                                val ratio = ((btnData.toDouble() - (KNOB_MAX / 2)) / (KNOB_MAX / 2)).toDouble()
                                ((size.width / 2) * ratio).toInt()
                            }
                            else -> 0
                        }
                        robot.mouseMove(x + offX, y + offY)
                    }
                    "Mouse Y" -> {
                        y = center.y + when {
                            btnData < KNOB_MAX / 2 -> {
                                // up
                                val ratio = 1.0 - (btnData.toDouble() / (KNOB_MAX / 2).toDouble())
                                -((size.height / 2) * ratio).toInt()
                            }
                            btnData > KNOB_MAX / 2 -> {
                                // down
                                val ratio = ((btnData.toDouble() - (KNOB_MAX / 2)) / (KNOB_MAX / 2)).toDouble()
                                ((size.height / 2) * ratio).toInt()
                            }
                            else -> 0
                        }
                        robot.mouseMove(x + offX, y + offY)
                    }
                    "Mouse offset X" -> {
                        offX = when {
                            btnData < KNOB_MAX / 2 -> {
                                // left
                                -(KNOB_MAX / 2) + btnData
                            }
                            btnData > KNOB_MAX / 2 -> {
                                // right
                                btnData - (KNOB_MAX / 2)
                            }
                            else -> 0
                        }
                        robot.mouseMove(x + offX, y + offY)
                    }
                    "Mouse offset Y" -> {
                        offY = when {
                            btnData < KNOB_MAX / 2 -> {
                                // up
                                -(KNOB_MAX / 2) + btnData
                            }
                            btnData > KNOB_MAX / 2 -> {
                                // down
                                btnData - (KNOB_MAX / 2)
                            }
                            else -> 0
                        }
                        robot.mouseMove(x + offX, y + offY)
                    }
                }
            }
            "Command" -> {
                val command = data.replace(
                    "\$data", btnData.toString()
                )
                println("Executing command: $command")
                Runtime.getRuntime().exec(command)
            }
        }
    }
}