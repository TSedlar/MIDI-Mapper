package me.sedlar.midi.binding.actions

class KnobAction : BasicActionHandler(
    "Knob Actions",
    arrayOf(
        "Left click",
        "Right click",
        "Mouse X",
        "Mouse Y",
        "Mouse offset X",
        "Mouse offset Y"
    ),
    runTypes = arrayOf("Knob")
)