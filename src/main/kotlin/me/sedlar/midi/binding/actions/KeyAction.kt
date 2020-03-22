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
                    "Esc" -> keyList.add(KeyEvent.VK_ESCAPE)
                    "Backspace" -> keyList.add(KeyEvent.VK_BACK_SPACE)
                    "Enter" -> keyList.add(KeyEvent.VK_ENTER)
                    "Windows" -> keyList.add(KeyEvent.VK_WINDOWS)
                    "Up" -> keyList.add(KeyEvent.VK_UP)
                    "Down" -> keyList.add(KeyEvent.VK_DOWN)
                    "Left" -> keyList.add(KeyEvent.VK_LEFT)
                    "Right" -> keyList.add(KeyEvent.VK_RIGHT)
                    "Caps Lock" -> keyList.add(KeyEvent.VK_CAPS_LOCK)
                    "Delete" -> keyList.add(KeyEvent.VK_DELETE)
                    "Home" -> keyList.add(KeyEvent.VK_HOME)
                    "End" -> keyList.add(KeyEvent.VK_END)
                    "Page Up" -> keyList.add(KeyEvent.VK_PAGE_UP)
                    "Page Down" -> keyList.add(KeyEvent.VK_PAGE_DOWN)
                    "Insert" -> keyList.add(KeyEvent.VK_INSERT)
                    "Print Screen" -> keyList.add(KeyEvent.VK_PRINTSCREEN)
                    "Scroll Lock" -> keyList.add(KeyEvent.VK_SCROLL_LOCK)
                    "Pause" -> keyList.add(KeyEvent.VK_PAUSE)
                    "Context Menu" -> keyList.add(KeyEvent.VK_CONTEXT_MENU)
                    "F1" -> keyList.add(KeyEvent.VK_F1)
                    "F2" -> keyList.add(KeyEvent.VK_F2)
                    "F3" -> keyList.add(KeyEvent.VK_F3)
                    "F4" -> keyList.add(KeyEvent.VK_F4)
                    "F5" -> keyList.add(KeyEvent.VK_F5)
                    "F6" -> keyList.add(KeyEvent.VK_F6)
                    "F7" -> keyList.add(KeyEvent.VK_F7)
                    "F8" -> keyList.add(KeyEvent.VK_F8)
                    "F9" -> keyList.add(KeyEvent.VK_F9)
                    "F10" -> keyList.add(KeyEvent.VK_F10)
                    "F11" -> keyList.add(KeyEvent.VK_F11)
                    "F12" -> keyList.add(KeyEvent.VK_F12)
                }
            }
        }
        if (repeat) {
            doWhileHeld(
                btnData,
                task = {
                    for (key in keyList) {
                        robot.keyPress(key)
                    }
                },
                finishTask = {
                    for (key in keyList.reversed()) {
                        robot.keyRelease(key)
                    }
                }
            )
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