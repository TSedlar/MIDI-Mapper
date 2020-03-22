package me.sedlar.midi.binding.actions

class KnobAction : BasicActionHandler(
    "Knob Actions",
    arrayOf(
        "Left click" to false,
        "Right click" to false,
        "Middle click" to true,
        "Drag" to true,
        "Mouse X" to false,
        "Mouse Y" to false,
        "Mouse offset X" to false,
        "Mouse offset Y" to false
    ),
    runTypes = arrayOf("Knob")
)