package me.sedlar.midi.binding.actions

import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.stage.FileChooser
import me.sedlar.midi.binding.MIDIAction
import me.sedlar.midi.binding.MIDIBinding
import me.sedlar.util.JFX
import java.io.File
import java.lang.Runnable

class SoundAction : MIDIAction("Sound") {

    override fun build(confirmer: Runnable): Pair<Node, Runnable> {
        val node = JFX.loadFXML("/bindings/sound.fxml")
        val btnSound = node.lookup("#btn-sound") as Button
        btnSound.setOnAction {
            val fileChooser = FileChooser()
            fileChooser.showOpenDialog(null)?.let { file ->
                formData = file.absolutePath
                btnSound.text = file.name
            }
            confirmer.run()
        }
        return node to Runnable {
            btnSound.text = formData
        }
    }

    override fun execute(binding: MIDIBinding, btnData: Byte) {
        val hit = Media(File(binding.data).toURI().toString())
        val mediaPlayer = MediaPlayer(hit)
        mediaPlayer.play()
    }
}