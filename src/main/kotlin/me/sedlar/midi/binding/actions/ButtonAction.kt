package me.sedlar.midi.binding.actions

class ButtonAction : BasicActionHandler(
    "Button Actions",
    arrayOf(
        "Left click",
        "Right click"
    ),
    runTypes = arrayOf("Button")
)