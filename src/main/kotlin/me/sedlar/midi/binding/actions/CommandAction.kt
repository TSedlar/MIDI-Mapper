package me.sedlar.midi.binding.actions

import javafx.scene.Node
import javafx.scene.control.TextField
import me.sedlar.midi.binding.MIDIAction
import me.sedlar.midi.binding.MIDIBinding
import me.sedlar.util.JFX

class CommandAction : MIDIAction("Command") {

    override fun build(confirmer: Runnable): Pair<Node, Runnable> {
        val node = JFX.loadFXML("/bindings/command.fxml")
        val txtCommand = node.lookup("#txt-command") as TextField
        txtCommand.setOnKeyReleased {
            formData = txtCommand.text
            confirmer.run()
        }
        return node to Runnable {
            txtCommand.text = formData
        }
    }

    override fun execute(binding: MIDIBinding, btnData: Byte) {
        val command = binding.data
            .replace("\$data", btnData.toString())
        Runtime.getRuntime().exec(command)
    }
}