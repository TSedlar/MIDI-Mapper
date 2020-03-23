package me.sedlar.midi

import me.sedlar.midi.display.LaunchkeyMiniDisplay

object DisplayManager {

    val DISPLAYS = arrayOf<MIDIDisplay>(
        LaunchkeyMiniDisplay()
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