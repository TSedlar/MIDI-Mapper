package me.sedlar.midi

import me.sedlar.midi.binding.MIDIBinding
import java.io.File

class DeviceProfile(val deviceName: String, val name: String) {

    val location = File(ProfileManager.DIRECTORY, deviceName + File.separator + name + ".json")

    val bindings = ArrayList<MIDIBinding>()
}