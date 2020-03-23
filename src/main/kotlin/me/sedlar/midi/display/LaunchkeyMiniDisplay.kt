package me.sedlar.midi.display

import me.sedlar.midi.KeyDrawData
import me.sedlar.midi.MIDIDisplay
import java.awt.Color
import java.awt.Dimension
import java.awt.Image
import java.awt.Point
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

class LaunchkeyMiniDisplay : MIDIDisplay("Launchkey Mini") {

    override fun createMapping(): Map<Int, Array<KeyDrawData>> {
        return mapOf(
            // White keys (48, 50, 52, 53, 55, 57, 59, 60, 62, 64, 65, 67, 69, 71, 72)
            48 to arrayOf(
                KeyDrawData(145, 518, 145, 573, Point(145, 590)),
                KeyDrawData(-1, -1, -1, -1, Point(511, 232))
            ),
            50 to arrayOf(
                KeyDrawData(192, 518, 192, 558, Point(192, 575)),
                KeyDrawData(-1, -1, -1, -1, Point(618, 232))
            ),
            52 to arrayOf(KeyDrawData(239, 518, 239, 573, Point(239, 590))),
            53 to arrayOf(KeyDrawData(286, 518, 286, 558, Point(286, 575))),
            55 to arrayOf(KeyDrawData(332, 518, 332, 573, Point(332, 590))),
            57 to arrayOf(KeyDrawData(378, 518, 378, 558, Point(378, 575))),
            59 to arrayOf(KeyDrawData(424, 518, 424, 573, Point(424, 590))),
            60 to arrayOf(KeyDrawData(470, 518, 470, 558, Point(470, 575))),
            62 to arrayOf(KeyDrawData(516, 518, 516, 573, Point(516, 590))),
            64 to arrayOf(KeyDrawData(562, 518, 562, 558, Point(562, 575))),
            65 to arrayOf(KeyDrawData(608, 518, 608, 573, Point(608, 590))),
            67 to arrayOf(KeyDrawData(654, 518, 654, 558, Point(654, 575))),
            69 to arrayOf(KeyDrawData(700, 518, 700, 573, Point(700, 590))),
            71 to arrayOf(KeyDrawData(746, 518, 746, 558, Point(746, 575))),
            72 to arrayOf(KeyDrawData(792, 518, 792, 573, Point(792, 590))),
            // Black keys (49, 51, 54, 56, 58, 61, 63, 66, 68, 70)
            49 to arrayOf(
                KeyDrawData(166, 339, 166, 370, Point(166, 333), color = Color.YELLOW),
                KeyDrawData(-1, -1, -1, -1, Point(565, 232))
            ),
            51 to arrayOf(
                KeyDrawData(220, 339, 220, 370, Point(220, 333), color = Color.YELLOW),
                KeyDrawData(-1, -1, -1, -1, Point(672, 232))
            ),
            54 to arrayOf(KeyDrawData(303, 339, 303, 370, Point(303, 333), color = Color.YELLOW)),
            56 to arrayOf(KeyDrawData(355, 339, 355, 370, Point(355, 333), color = Color.YELLOW)),
            58 to arrayOf(KeyDrawData(407, 339, 407, 370, Point(407, 333), color = Color.YELLOW)),
            61 to arrayOf(KeyDrawData(490, 339, 490, 370, Point(490, 333), color = Color.YELLOW)),
            63 to arrayOf(KeyDrawData(545, 339, 545, 370, Point(545, 333), color = Color.YELLOW)),
            66 to arrayOf(KeyDrawData(628, 339, 628, 370, Point(628, 333), color = Color.YELLOW)),
            68 to arrayOf(KeyDrawData(680, 339, 680, 370, Point(680, 333), color = Color.YELLOW)),
            70 to arrayOf(KeyDrawData(733, 339, 733, 370, Point(733, 333), color = Color.YELLOW)),
            // Knob (21-28)
            21 to arrayOf(KeyDrawData(297, 125, 297, 142, Point(297, 118), color = Color.RED)),
            22 to arrayOf(KeyDrawData(351, 125, 351, 142, Point(351, 118), color = Color.RED)),
            23 to arrayOf(KeyDrawData(404, 125, 404, 142, Point(404, 118), color = Color.RED)),
            24 to arrayOf(KeyDrawData(457, 125, 457, 142, Point(457, 118), color = Color.RED)),
            25 to arrayOf(KeyDrawData(511, 125, 511, 142, Point(511, 118), color = Color.RED)),
            26 to arrayOf(KeyDrawData(565, 125, 565, 142, Point(565, 118), color = Color.RED)),
            27 to arrayOf(KeyDrawData(618, 125, 618, 142, Point(618, 118), color = Color.RED)),
            28 to arrayOf(KeyDrawData(672, 125, 672, 142, Point(672, 118), color = Color.RED)),
            // Pads (40-43, 48-51, 36-39, 44-47)
            40 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(297, 232))),
            41 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(352, 232))),
            42 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(405, 232))),
            43 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(458, 232))),
            36 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(297, 286))),
            37 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(352, 286))),
            38 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(405, 286))),
            39 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(458, 286))),
            44 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(511, 286))),
            45 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(565, 286))),
            46 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(618, 286))),
            47 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(672, 286))),
            // Right circles
            108 to arrayOf(KeyDrawData(724, 206, 724, 216, Point(724, 200), color = Color.ORANGE)),
            109 to arrayOf(KeyDrawData(732, 290, 749, 308, Point(766, 320), color = Color.ORANGE)),
            // Scene
            104 to arrayOf(KeyDrawData(787, 224, 787, 230, Point(787, 219), color = Color.ORANGE)),
            105 to arrayOf(KeyDrawData(787, 279, 787, 285, Point(787, 300), color = Color.ORANGE)),
            // Track
            106 to arrayOf(KeyDrawData(144, 214, 144, 220, Point(144, 208), color = Color.ORANGE)),
            107 to arrayOf(KeyDrawData(188, 214, 188, 220, Point(188, 208), color = Color.ORANGE))
        )
    }

    override fun displaySize(): Dimension {
        return Dimension(950, 650)
    }

    override fun createImage(): Image {
        val img = ImageIO.read(javaClass.getResourceAsStream("/devices/launchkey-mini.jpg"))
        return img.getScaledInstance(875, 583, BufferedImage.SCALE_SMOOTH)
    }
}