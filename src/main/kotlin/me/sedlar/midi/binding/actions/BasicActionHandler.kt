package me.sedlar.midi.binding.actions

import me.sedlar.ext.mouseClick
import me.sedlar.midi.binding.DropdownAction
import me.sedlar.midi.binding.KNOB_MAX
import me.sedlar.midi.binding.MIDIBinding
import me.sedlar.midi.binding.robot
import java.awt.GraphicsEnvironment
import java.awt.Point

private val size = GraphicsEnvironment.getLocalGraphicsEnvironment().maximumWindowBounds
private val center = Point(size.width / 2, size.height / 2)

private var x = center.x
private var y = center.y
private var offX = 0
private var offY = 0

open class BasicActionHandler(
    name: String,
    actions: Array<String>,
    runTypes: Array<String> = arrayOf("Button", "Knob")
) : DropdownAction(name, actions, runTypes) {

    override fun execute(binding: MIDIBinding, btnData: Byte) {
        when (binding.data) {
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
}