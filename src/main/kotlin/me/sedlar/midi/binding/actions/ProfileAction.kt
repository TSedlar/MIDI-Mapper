package me.sedlar.midi.binding.actions

import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.TextField
import me.sedlar.midi.MidiMapper
import me.sedlar.midi.binding.MIDIAction
import me.sedlar.midi.binding.MIDIBinding
import me.sedlar.util.JFX

class ProfileAction : MIDIAction("Change Profile") {

    override fun build(confirmer: Runnable): Pair<Node, Runnable> {
        val node = JFX.loadFXML("/bindings/command.fxml")
        val lblCommand = node.lookup("#lbl-command") as Label
        val txtCommand = node.lookup("#txt-command") as TextField
        lblCommand.text = "Profile:"
        txtCommand.promptText = "Type a profile..."
        txtCommand.setOnKeyReleased {
            formData = txtCommand.text
            confirmer.run()
        }
        return node to Runnable {
            txtCommand.text = formData
        }
    }

    override fun execute(binding: MIDIBinding, btnData: Byte) {
        MidiMapper.INSTANCE.changeProfile(binding.data)
    }
}