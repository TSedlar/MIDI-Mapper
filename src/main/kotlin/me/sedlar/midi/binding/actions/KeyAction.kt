package me.sedlar.midi.binding.actions

import javafx.scene.Node
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import me.sedlar.midi.binding.DATA_BTN_IDLE
import me.sedlar.midi.binding.MIDIAction
import me.sedlar.midi.binding.MIDIBinding
import me.sedlar.midi.binding.robot
import me.sedlar.util.JFX
import me.sedlar.util.Keyboard
import java.awt.event.KeyEvent
import kotlin.random.Random

open class KeyAction(name: String = "Key"): MIDIAction(name) {

    internal var repeat: Boolean = false

    private var looping = false
    private var released = false

    override fun build(confirmer: Runnable): Pair<Node, Runnable> {
        val node = JFX.loadFXML("/bindings/key.fxml")
        val txtKey = node.lookup("#txt-key") as TextField

        val keyCombo = ArrayList<String>()
        var firstKey: KeyCode? = null

        // Handle "Key" output
        txtKey.setOnKeyPressed { evt ->
            if (firstKey == null) {
                firstKey = evt.code
            }
            val txt = evt.text.trim()
            val visual = if (txt.isEmpty()) evt.code.getName() else txt
            if (!keyCombo.contains(visual)) {
                keyCombo.add(visual)
            }
            evt.consume()
            confirmer.run()
        }

        txtKey.setOnKeyReleased { evt ->
            if (evt.code == firstKey) {
                val txt = keyCombo.joinToString(separator = " + ")
                formData = txt
                txtKey.text = txt
                keyCombo.clear()
                firstKey = null
            }
            confirmer.run()
        }
        return node to Runnable {
            txtKey.text = formData
        }
    }

    override fun execute(binding: MIDIBinding, btnData: Byte) {
        val keys = binding.data.split(" + ")
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
        if (repeat) {
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
}