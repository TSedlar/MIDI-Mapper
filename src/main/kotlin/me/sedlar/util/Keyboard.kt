package me.sedlar.util

import java.awt.AWTKeyStroke
import java.awt.AWTKeyStroke.getAWTKeyStroke
import java.awt.Robot
import java.awt.event.InputEvent.SHIFT_DOWN_MASK
import java.awt.event.KeyEvent

object Keyboard {

    private val robot: Robot = Robot()

    fun type(chars: CharSequence, ms: Int = 0) {
        var millis = ms
        millis = if (millis > 0) millis else 0
        var i = 0
        val len = chars.length
        while (i < len) {
            val c = chars[i]
            val keyStroke = getKeyStroke(c)
            val keyCode = keyStroke.keyCode
            val shift = Character.isUpperCase(c) || keyStroke.modifiers == SHIFT_DOWN_MASK + 1
            if (shift) {
                robot.keyPress(KeyEvent.VK_SHIFT)
            }

            robot.keyPress(keyCode)
            robot.keyRelease(keyCode)

            if (shift) {
                robot.keyRelease(KeyEvent.VK_SHIFT)
            }
            if (millis > 0) {
                robot.delay(millis)
            }
            i++
        }
    }

    fun getKeyStroke(c: Char): AWTKeyStroke {
        val upper = "`~'\"!@#$%^&*()_+{}|:<>?"
        val lower = "`~'\"1234567890-=[]\\;,./"

        val index = upper.indexOf(c)
        if (index != -1) {
            val keyCode: Int
            var shift = false
            when (c) {
                // these chars need to be handled specially because
                // they don't directly translate into the correct keycode
                '~' -> {
                    shift = true
                    keyCode = KeyEvent.VK_BACK_QUOTE
                }
                '`' -> keyCode = KeyEvent.VK_BACK_QUOTE
                '\"' -> {
                    shift = true
                    keyCode = KeyEvent.VK_QUOTE
                }
                '\'' -> keyCode = KeyEvent.VK_QUOTE
                else -> {
                    keyCode = Character.toUpperCase(lower[index]).toInt()
                    shift = true
                }
            }
            return getAWTKeyStroke(keyCode, if (shift) SHIFT_DOWN_MASK else 0)
        }
        return getAWTKeyStroke(Character.toUpperCase(c).toInt(), 0)
    }
}