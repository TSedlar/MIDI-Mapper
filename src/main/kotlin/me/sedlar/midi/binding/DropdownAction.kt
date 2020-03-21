package me.sedlar.midi.binding

import javafx.scene.Node
import javafx.scene.control.ChoiceBox
import me.sedlar.util.JFX

abstract class DropdownAction(
    name: String,
    val actions: Array<String>,
    runTypes: Array<String> = arrayOf("Button", "Knob")
) : MIDIAction(name, runTypes) {

    private var cmbAction: ChoiceBox<String>? = null

    @Suppress("UNCHECKED_CAST")
    override fun build(confirmer: Runnable): Pair<Node, Runnable> {
        val node = JFX.loadFXML("/bindings/action.fxml")
        cmbAction = node.lookup("#cmb-actions") as ChoiceBox<String>
        cmbAction?.items?.addAll(actions)
        cmbAction?.selectionModel?.selectedItemProperty()?.addListener { _, _, newValue ->
            formData = newValue
            confirmer.run()
        }
        return node to Runnable {
            cmbAction?.selectionModel?.select(formData)
        }
    }
}