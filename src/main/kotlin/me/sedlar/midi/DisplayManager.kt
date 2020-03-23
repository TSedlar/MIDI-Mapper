package me.sedlar.midi

import me.sedlar.midi.display.LaunchkeyMiniDisplay
import me.sedlar.midi.display.LaunchpadMiniDisplay

object DisplayManager {

    val DISPLAYS = arrayOf(
        LaunchkeyMiniDisplay(),
        LaunchpadMiniDisplay()
    )

    fun findDisplay(device: String): MIDIDisplay? {
        for (display in DISPLAYS) {
            if (display.deviceName == device) {
                return display
            }
        }
        return null
    }

    fun hasDisplayForDevice(device: String): Boolean {
        return findDisplay(device) != null
    }
}