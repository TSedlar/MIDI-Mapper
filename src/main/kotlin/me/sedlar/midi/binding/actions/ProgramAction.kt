package me.sedlar.midi.binding.actions

import javafx.scene.Node
import javafx.scene.control.Button
import javafx.stage.FileChooser
import me.sedlar.midi.binding.MIDIAction
import me.sedlar.midi.binding.MIDIBinding
import me.sedlar.util.JFX
import java.awt.Desktop
import java.io.File

class ProgramAction : MIDIAction("Program") {

    override fun build(confirmer: Runnable): Pair<Node, Runnable> {
        val node = JFX.loadFXML("/bindings/program.fxml")
        val btnProgram = node.lookup("#btn-program") as Button
        btnProgram.setOnAction {
            val fileChooser = FileChooser()
            fileChooser.showOpenDialog(null)?.let { file ->
                formData = file.absolutePath
                btnProgram.text = file.name
            }
            confirmer.run()
        }
        return node to Runnable {
            btnProgram.text = formData
        }
    }

    override fun execute(binding: MIDIBinding, btnData: Byte) {
        Desktop.getDesktop().open(File(binding.data))
    }
}