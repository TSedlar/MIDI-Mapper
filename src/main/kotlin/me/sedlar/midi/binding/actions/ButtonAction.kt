package me.sedlar.midi.binding.actions

class ButtonAction : BasicActionHandler(
    "Button Actions",
    arrayOf(
        "Left click" to false,
        "Right click" to false,
        "Middle click" to true,
        "Drag" to true
    ),
    runTypes = arrayOf("Button")
)