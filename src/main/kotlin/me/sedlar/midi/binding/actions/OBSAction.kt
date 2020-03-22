package me.sedlar.midi.binding.actions

import javafx.scene.Node
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import me.sedlar.midi.binding.DropdownAction
import me.sedlar.midi.binding.MIDIBinding
import me.sedlar.util.JFX
import net.twasi.obsremotejava.OBSRemoteController

class OBSAction : DropdownAction(
    "OBS",
    arrayOf(
        "Change profile",
        "Change scene",
        "Show source",
        "Hide source",
        "Mute source",
        "Unmute source",
        "Change volume"
    )
) {

    private var controller: OBSRemoteController? = null

    @Suppress("UNCHECKED_CAST")
    override fun build(confirmer: Runnable): Pair<Node, Runnable> {
        val parent = VBox()
        parent.spacing = 10.0

        val dropdown = JFX.loadFXML("/bindings/action.fxml")
        cmbAction = dropdown.lookup("#cmb-actions") as ChoiceBox<String>
        cmbAction?.items?.addAll(actions)
        cmbAction?.selectionModel?.selectedItemProperty()?.addListener { _, _, newValue ->
            formData = newValue
            confirmer.run()
        }

        val args = JFX.loadFXML("/bindings/args.fxml")
        val txtArgs = args.lookup("#txt-args") as TextField

        txtArgs.setOnKeyReleased {
            formArgs = txtArgs.text.split(" ").toTypedArray()
        }

        parent.children.add(dropdown)
        parent.children.add(args)

        return parent to Runnable {
            cmbAction?.selectionModel?.select(formData)
            txtArgs.text = formArgs.joinToString(separator = " ")
        }
    }

    override fun execute(binding: MIDIBinding, btnData: Byte) {
        if (controller == null) {
            controller = OBSRemoteController("ws://localhost:4444", false)
            if (controller != null && controller!!.isFailed) {
                throw RuntimeException("OBS Socket is not open...")
            }
        }
        val args = binding.args.joinToString(separator = " ")
        when (binding.data) {
            "Change profile" -> {
                controller?.setCurrentProfile(args) {}
            }
            "Change scene" -> {
                val data = args.split(",")
                if (data.size == 1) {
                    controller?.setCurrentScene(args) {}
                } else {
                    val scene = data[0]
                    val transition = data[1].trim()
                    controller?.changeSceneWithTransition(scene, transition) {}
                }
            }
            "Show source" -> {
                val data = args.split(",")
                val scene = data[0]
                val source = data[1].trim()
                controller?.setSourceVisibility(scene, source, true) {}
            }
            "Hide source" -> {
                val data = args.split(",")
                val scene = data[0]
                val source = data[1].trim()
                controller?.setSourceVisibility(scene, source, false) {}
            }
            "Mute source" -> {
                controller?.setMute(args, true) {}
            }
            "Unmute source" -> {
                controller?.setMute(args, false) {}
            }
            "Change volume" -> {
                if (binding.type == "Knob") {
                    var percent = (btnData.toDouble() / 127.0)
                    if (percent < 0.01) percent = 0.0
                    println(percent)
                    controller?.setVolume(args, percent) {}
                } else {
                    val data = args.split(",")
                    val source = data[0]
                    val volume = data[1].trim().toDouble()
                    controller?.setVolume(source, volume) {}
                }
            }
        }
    }
}