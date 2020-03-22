package me.sedlar.midi.binding.actions

import javafx.scene.Node
import javafx.scene.control.TextField
import me.sedlar.midi.binding.MIDIAction
import me.sedlar.midi.binding.MIDIBinding
import me.sedlar.util.JFX
import me.sedlar.util.Keyboard

open class MultiKeyAction(name: String = "Multikey") : MIDIAction(name) {

    override fun build(confirmer: Runnable): Pair<Node, Runnable> {
        val node = JFX.loadFXML("/bindings/multikey.fxml")
        val txtMultikey = node.lookup("#txt-multikey") as TextField
        txtMultikey.setOnKeyReleased {
            formData = txtMultikey.text
            confirmer.run()
        }
        return node to Runnable {
            txtMultikey.text = formData
        }
    }

    override fun execute(binding: MIDIBinding, btnData: Byte) {
        if (repeat) {
            doWhileHeld(
                btnData,
                task = {
                    Keyboard.type(binding.data)
                },
                finishTask = {}
            )
        } else {
            Keyboard.type(binding.data)
        }
    }
}