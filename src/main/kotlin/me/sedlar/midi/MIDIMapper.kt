package me.sedlar.midi

import javafx.application.Application
import javafx.application.Platform.runLater
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import javafx.stage.FileChooser
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle
import me.sedlar.midi.binding.MIDIBinding
import java.io.File
import javax.sound.midi.MidiDevice
import javax.sound.midi.MidiMessage
import javax.sound.midi.MidiSystem
import javax.sound.midi.Receiver
import kotlin.system.exitProcess
import javafx.scene.layout.VBox
import java.awt.Desktop


private fun findDevices(): List<MidiDevice> {
    val list = ArrayList<MidiDevice>()
    val infoList = MidiSystem.getMidiDeviceInfo()
    for (info in infoList) {
        val device = MidiSystem.getMidiDevice(info)
        if (device.maxTransmitters != 0) {
            list.add(device)
        }
    }
    return list
}

@Suppress("UNCHECKED_CAST")
class MidiMapper : Application() {

    private val devices = ArrayList<MidiDevice>()
    private var root: Scene? = null
    private var rootStage: Stage? = null
    private val cmbDevices: ComboBox<String>?
        get() = root?.lookup("#cmb-devices") as ComboBox<String>
    private val lstProfiles: ListView<String>?
        get() = root?.lookup("#lst-profiles") as ListView<String>
    private val btnProfileAdd: Button?
        get() = root?.lookup("#btn-profile-add") as Button
    private val btnProfileRename: Button?
        get() = root?.lookup("#btn-profile-rename") as Button
    private val btnProfileDelete: Button?
        get() = root?.lookup("#btn-profile-delete") as Button
    private val lstBindings: ListView<String>?
        get() = root?.lookup("#lst-bindings") as ListView<String>
    private val btnBindingAdd: Button?
        get() = root?.lookup("#btn-binding-add") as Button
    private val btnBindingEdit: Button?
        get() = root?.lookup("#btn-binding-edit") as Button
    private val btnBindingDelete: Button?
        get() = root?.lookup("#btn-binding-delete") as Button

    override fun start(stage: Stage) {
        val parent = FXMLLoader.load<Parent>(javaClass.getResource("/design-main.fxml"))

        val menuBar = MenuBar()

        val fileMenu = Menu("File")

        val dirItem = MenuItem("Open Directory")
        val exitItem = MenuItem("Exit")

        dirItem.setOnAction {
            Desktop.getDesktop().browse(ProfileManager.DIRECTORY.parentFile.toURI())
        }

        exitItem.setOnAction {
            exitProcess(0)
        }

        fileMenu.items.add(dirItem)
        fileMenu.items.add(exitItem)

        menuBar.menus.add(fileMenu)

        val vBox = VBox(menuBar)

        vBox.children.add(parent)

        root = Scene(vBox)
        rootStage = stage

        stage.icons.add(Image(javaClass.getResourceAsStream("/midi-icon.png")));

        stage.scene = root
        stage.title = "MIDI Mapper"
        stage.show()
        stage.width = stage.width - 10.0
        stage.height = stage.height - 10.0
        stage.isResizable = false

        stage.setOnCloseRequest {
            exitProcess(0)
        }

        runLater {
            setup()
        }
    }

    private fun findSelectedDevice(): MidiDevice? {
        val deviceName = cmbDevices?.selectionModel?.selectedItem
        for (device in devices) {
            if (device.deviceInfo.name == deviceName) {
                return device
            }
        }
        return null
    }

    private fun setup() {
        devices.clear()
        devices.addAll(findDevices())

        cmbDevices?.items?.addAll(devices.map { it.deviceInfo.name })

        cmbDevices?.selectionModel?.selectedItemProperty()?.addListener { _, oldValue, newValue ->
            devices.find { it.deviceInfo.name == oldValue }?.close()
            handleDeviceLoad()
        }

        lstProfiles?.selectionModel?.selectedItemProperty()?.addListener { _, _, newValue ->
            handleProfileLoad()
        }

        btnProfileAdd?.setOnAction {
            handleProfileAdd()
        }

        btnProfileRename?.setOnAction {
            handleProfileRename()
        }

        btnProfileDelete?.setOnAction {
            handleProfileDelete()
        }

        btnBindingAdd?.setOnAction {
            handleBindingCreate { binding ->
                if (binding != null) {
                    findSelectedProfile()?.bindings?.add(binding)
                    handleProfileLoad()
                }
                attachWithProfile()
            }
        }

        lstBindings?.selectionModel?.selectedItemProperty()?.addListener { _, _, newValue ->
            btnBindingEdit?.isDisable = false
            btnBindingDelete?.isDisable = false
        }

        btnBindingEdit?.setOnAction {
            handleBindingEdit()
        }

        btnBindingDelete?.setOnAction {
            handleBindingDelete()
        }
    }

    private fun handleDeviceLoad() {
        lstProfiles?.items?.clear()
        btnProfileAdd?.isDisable = true
        btnProfileRename?.isDisable = true
        btnProfileDelete?.isDisable = true
        findSelectedDevice()?.let { device ->
            device.close()
            ProfileManager.loadProfiles(device.deviceInfo.name)
            ProfileManager.PROFILES[device.deviceInfo.name]?.forEach { profile ->
                lstProfiles?.items?.add(profile.name)
            }
            btnProfileAdd?.isDisable = false
        }
    }

    private fun handleProfileLoad() {
        lstBindings?.items?.clear()
        btnProfileRename?.isDisable = true
        btnProfileDelete?.isDisable = true
        btnBindingAdd?.isDisable = true
        btnBindingEdit?.isDisable = true
        btnBindingDelete?.isDisable = true
        findSelectedDevice()?.close()
        findSelectedProfile()?.let { profile ->
            profile.bindings.forEach { binding ->
                lstBindings?.items?.add("${binding.btn} [${binding.trigger}] -> ${binding.output} [${binding.data}]")
            }
            btnProfileRename?.isDisable = false
            btnProfileDelete?.isDisable = false
            btnBindingAdd?.isDisable = false
        }
        attachWithProfile()
    }

    private fun handleProfileAdd() {
        val dialog = TextInputDialog("")

        dialog.title = "MIDI Mapper"
        dialog.headerText = "Enter profile name:"
        dialog.contentText = "Name:"

        val result = dialog.showAndWait()

        result.ifPresent { profileName ->
            if (profileName.isNotEmpty()) {
                findSelectedDevice()?.let { device ->
                    ProfileManager.createOrGetProfile(device.deviceInfo.name, profileName)
                    println("Created profile [$profileName] @ [${device.deviceInfo.name}]")
                    lstProfiles?.items?.add(profileName)
                    lstProfiles?.selectionModel?.select(profileName)
                }
            }
        }
    }

    private fun handleProfileRename() {
        findSelectedProfile()?.let { profile ->
            val selectedIndex = lstProfiles!!.selectionModel.selectedIndex
            val dialog = TextInputDialog("")

            dialog.title = "MIDI Mapper"
            dialog.headerText = "Enter profile name:"
            dialog.contentText = "Name:"

            val result = dialog.showAndWait()

            result.ifPresent { profileName ->
                val newProfile = DeviceProfile(profile.deviceName, profileName)
                newProfile.bindings.addAll(profile.bindings)
                ProfileManager.deleteProfile(profile)?.let { idx ->
                    ProfileManager.PROFILES[profile.deviceName]?.add(idx, newProfile)
                }
                ProfileManager.saveProfile(newProfile)
                lstProfiles?.items?.set(selectedIndex, profileName)
                lstProfiles?.selectionModel?.select(selectedIndex)
            }
        }
    }

    private fun handleProfileDelete() {
        findSelectedDevice()?.let { device ->
            val profileName = lstProfiles?.selectionModel?.selectedItem
            ProfileManager.createOrGetProfile(device.deviceInfo.name, profileName)
                ?.let { profile ->
                    val alert = Alert(AlertType.CONFIRMATION)
                    alert.title = "Delete Profile"
                    alert.headerText = "Are you sure want to delete this profile?"
                    alert.contentText = "Profile: [$profileName] @ [${device.deviceInfo.name}]"

                    // option != null.
                    val option = alert.showAndWait()

                    if (option.get() == ButtonType.OK) {
                        ProfileManager.deleteProfile(profile)
                        println("Deleted profile [$profileName] @ [${device.deviceInfo.name}]")
                        lstProfiles?.selectionModel?.clearSelection()
                        lstProfiles?.items?.remove(profileName)
                        btnProfileRename?.isDisable = true
                        btnProfileDelete?.isDisable = true
                    }
                }
        }
    }

    private fun handleBindingCreate(
        defaultBtn: String? = null,
        defaultType: String? = null,
        defaultOutput: String? = null,
        defaultData: String? = null,
        callback: (binding: MIDIBinding?) -> Unit
    ) {
        val parent = FXMLLoader.load<Parent>(javaClass.getResource("/design-binding.fxml"))

        val stage = Stage()
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.initStyle(StageStyle.UNDECORATED)
        stage.title = "Binding"

        val scene = Scene(parent)
        stage.scene = scene

        val grpKey = scene.lookup("#grp-key")
        val grpMultikey = scene.lookup("#grp-multikey")
        val grpProgram = scene.lookup("#grp-program")
        val grpAction = scene.lookup("#grp-action")
        val grpCommand = scene.lookup("#grp-command")

        val lblMidi = scene.lookup("#lbl-midi") as Label
        val cmbType = scene.lookup("#cmb-type") as ChoiceBox<String>
        val cmbOutput = scene.lookup("#cmb-output") as ChoiceBox<String>
        val txtKey = scene.lookup("#txt-key") as TextField
        val txtMultikey = scene.lookup("#txt-multikey") as TextField
        val btnProgram = scene.lookup("#btn-program") as Button
        val cmbActions = scene.lookup("#cmb-actions") as ChoiceBox<String>
        val txtCommand = scene.lookup("#txt-command") as TextField

        val btnConfirm = scene.lookup("#btn-binding-confirm") as Button
        val btnCancel = scene.lookup("#btn-binding-cancel") as Button

        var data: String? = null
        var args: String? = null

        cmbType.items.addAll("Button", "Knob")

        cmbType.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            cmbOutput.selectionModel.clearSelection()
            cmbOutput.items.clear()
            cmbOutput.items.addAll(
                "Key", "Repeat key", "Multikey", "Program", "Action", "Command"
            )
            if (newValue == "Button") {
                cmbActions.items.addAll("Left click", "Right click")
            } else {
                cmbActions.items.addAll(
                    "Left click",
                    "Right click",
                    "Mouse X",
                    "Mouse Y",
                    "Mouse offset X",
                    "Mouse offset Y"
                )
            }
        }

        val resetter = Runnable {
            data = null
            args = null
            grpKey.isManaged = false
            grpKey.isVisible = false
            grpMultikey.isManaged = false
            grpMultikey.isVisible = false
            grpProgram.isManaged = false
            grpProgram.isVisible = false
            grpAction.isManaged = false
            grpAction.isVisible = false
            grpCommand.isManaged = false
            grpCommand.isVisible = false
        }

        val enabler = Runnable {
            btnConfirm.isDisable = !(data != null && data!!.isNotEmpty() && lblMidi.text.contains("["))
        }

        resetter.run()

        cmbOutput.selectionModel?.selectedItemProperty()?.addListener { _, _, newValue ->
            resetter.run()
            when (newValue) {
                "Repeat key",
                "Key" -> {
                    grpKey.isManaged = true
                    grpKey.isVisible = true
                }
                "Multikey" -> {
                    grpMultikey.isManaged = true
                    grpMultikey.isVisible = true
                }
                "Program" -> {
                    grpProgram.isManaged = true
                    grpProgram.isVisible = true
                }
                "Action" -> {
                    grpAction.isManaged = true
                    grpAction.isVisible = true
                }
                "Command" -> {
                    grpCommand.isManaged = true
                    grpCommand.isVisible = true
                }
            }
            stage.sizeToScene()
        }

        cmbType.selectionModel.selectFirst()

        findSelectedDevice()?.let { device ->
            device.close()
            device.transmitter?.receiver = object : Receiver {
                override fun close() {
                }

                override fun send(message: MidiMessage, timeStamp: Long) {
                    if (message.length != 3) return
                    val arg0 = message.message[0]
                    val btn = message.message[1]
                    val msgData = message.message[2]
                    runLater {
                        lblMidi.text = "$btn [$msgData]"
                    }
                }
            }
            device.open()
        }

        // Handle enabling when changing midi input
        lblMidi.textProperty().addListener { _, _, _ ->
            enabler.run()
        }

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
            enabler.run()
        }

        txtKey.setOnKeyReleased { evt ->
            if (evt.code == firstKey) {
                val txt = keyCombo.joinToString(separator = " + ")
                data = txt
                txtKey.text = txt
                keyCombo.clear()
                firstKey = null
            }
            enabler.run()
        }

        // Handle "Multikey" output
        txtMultikey.setOnKeyReleased {
            data = txtMultikey.text
            enabler.run()
        }

        // Handle "Program" output
        btnProgram.setOnAction {
            // file chooser..
            val fileChooser = FileChooser()
            fileChooser.showOpenDialog(rootStage)?.let { file ->
                data = file.absolutePath
                btnProgram.text = file.name
            }
            enabler.run()
        }

        // Handle "Action" output
        cmbActions.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            data = newValue
            enabler.run()
        }

        // Handle "Command" output
        txtCommand.setOnKeyReleased {
            data = txtCommand.text
            enabler.run()
        }

        btnConfirm.setOnAction {
            val midiData = lblMidi.text.split(" [")
            val btn = midiData[0].toByte()
            val trigger = if (midiData[1] != "0]") {
                midiData[1].substring(0, midiData[1].length - 1)
            } else {
                "!0"
            }
            val type = cmbType.selectionModel.selectedItem.toLowerCase()
            val output = cmbOutput.selectionModel.selectedItem

            callback(
                MIDIBinding(
                    btn = btn,
                    trigger = if (data!!.contains("Mouse")) "!0" else trigger,
                    type = type,
                    output = output,
                    data = data!!,
                    args = args
                )
            )

            stage.close()
        }

        btnCancel.setOnAction {
            stage.close()
            callback(null)
        }

        if (defaultBtn != null) {
            lblMidi.text = defaultBtn
            enabler.run()
        }

        if (defaultType != null) {
            cmbType.selectionModel.select(defaultType)
            enabler.run()
        }

        if (defaultOutput != null) {
            cmbOutput.selectionModel.select(defaultOutput)
            when (defaultOutput) {
                "Repeat key",
                "Key" -> {
                    txtKey.text = defaultData
                }
                "Multikey" -> {
                    txtMultikey.text = defaultData
                }
                "Program" -> {
                    val file = File(defaultData!!)
                    data = file.absolutePath
                    btnProgram.text = file.name
                }
                "Action" -> {
                    cmbActions.selectionModel.select(defaultData)
                }
                "Command" -> {
                    txtCommand.text = defaultData
                }
            }
            enabler.run()
        }

        if (defaultData != null) {
            data = defaultData
            enabler.run()
        }

        stage.show()
    }

    private fun handleBindingEdit() {
        findSelectedProfile()?.bindings?.let { bindings ->
            lstBindings?.selectionModel?.selectedIndex?.let { idx ->
                bindings.getOrNull(idx)?.let { binding ->
                    handleBindingCreate(
                        defaultBtn = "${binding.btn} [${binding.trigger}]",
                        defaultType = binding.type,
                        defaultOutput = binding.output,
                        defaultData = binding.data,
                        callback = { newBinding ->
                            if (newBinding != null) {
                                bindings[idx] = newBinding
                                handleProfileLoad()
                            }
                        }
                    )
                }
            }
        }
    }

    private fun handleBindingDelete() {
        lstBindings?.selectionModel?.selectedItem?.let { binding ->
            val selectionIdx = lstBindings?.selectionModel?.selectedIndex
            findSelectedProfile()?.let { profile ->
                profile.bindings.removeAt(selectionIdx!!)
                lstBindings?.selectionModel?.clearSelection()
                lstBindings?.items?.remove(binding)
                btnBindingDelete?.isDisable = true
                btnBindingEdit?.isDisable = true
            }
        }
    }

    private fun findSelectedProfile(): DeviceProfile? {
        val device = findSelectedDevice()
        val profile = lstProfiles?.selectionModel?.selectedItem
        if (device != null && profile != null) {
            return ProfileManager.createOrGetProfile(device.deviceInfo.name, profile)
        }
        return null
    }

    private fun attachWithProfile() {
        findSelectedDevice()?.let { device ->
            device.close()
            findSelectedProfile()?.let { profile ->
                device.transmitter?.receiver = object : Receiver {
                    override fun close() {
                    }

                    override fun send(message: MidiMessage, timeStamp: Long) {
                        if (message.length != 3) return
                        val arg0 = message.message[0]
                        val btn = message.message[1]
                        val msgData = message.message[2]
                        var mapped = false

                        profile.bindings.forEach { binding ->
                            if (binding.btn == btn) {
                                val isRepeatKey = binding.output == "Repeat key"
                                if ((isRepeatKey || binding.trigger == "!0" && msgData != 0.toByte()) || binding.trigger == msgData.toString()) {
                                    try {
                                        binding.execute(msgData)
                                    } catch (t: Throwable) {
                                        t.printStackTrace()
                                    }
                                    mapped = true
                                }
                            }
                        }

                        if (!mapped && msgData != 0.toByte()) {
                            println("Unmapped @ $btn [$msgData]")
                        }
                    }
                }
                device.open()
            }
        }
    }
}


fun main() {
    Application.launch(MidiMapper::class.java)
}