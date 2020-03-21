package me.sedlar.ext

import java.awt.Robot
import java.awt.event.InputEvent

fun Robot.mouseClick(left: Boolean) {
    if (left) {
        this.mousePress(InputEvent.BUTTON1_DOWN_MASK)
        this.mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
    } else {
        this.mousePress(InputEvent.BUTTON3_DOWN_MASK)
        this.mouseRelease(InputEvent.BUTTON3_DOWN_MASK)
    }
}

fun Robot.doKey(keyCode: Int) {
    this.keyPress(keyCode)
    this.keyRelease(keyCode)
}