package me.sedlar.midi.display

import me.sedlar.midi.KeyDrawData
import me.sedlar.midi.MIDIDisplay
import java.awt.Color
import java.awt.Dimension
import java.awt.Image
import java.awt.Point
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

class LaunchpadMiniDisplay : MIDIDisplay("Launchpad Mini") {

    override fun createMapping(): Map<Int, Array<KeyDrawData>> {
        return mapOf(
            // buttons
            //   row A
            0 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(173, 220))),
            1 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(240, 220))),
            2 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(305, 220))),
            3 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(372, 220))),
            4 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(438, 220))),
            5 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(504, 220))),
            6 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(570, 220))),
            7 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(637, 220))),
            //  row B
            16 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(173, 287))),
            17 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(240, 287))),
            18 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(305, 287))),
            19 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(372, 287))),
            20 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(438, 287))),
            21 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(504, 287))),
            22 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(570, 287))),
            23 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(637, 287))),
            //  row C
            32 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(173, 353))),
            33 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(240, 353))),
            34 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(306, 353))),
            35 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(372, 353))),
            36 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(438, 353))),
            37 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(504, 353))),
            38 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(570, 353))),
            39 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(637, 353))),
            //  row D
            48 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(174, 419))),
            49 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(241, 419))),
            50 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(306, 419))),
            51 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(373, 419))),
            52 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(439, 419))),
            53 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(505, 419))),
            54 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(571, 419))),
            55 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(637, 419))),
            //  row E
            64 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(174, 485))),
            65 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(241, 485))),
            66 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(306, 485))),
            67 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(373, 485))),
            68 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(439, 485))),
            69 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(505, 485))),
            70 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(571, 485))),
            71 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(637, 485))),
            //  row F
            80 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(174, 551))),
            81 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(241, 551))),
            82 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(306, 551))),
            83 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(373, 551))),
            84 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(439, 551))),
            85 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(505, 551))),
            86 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(571, 551))),
            87 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(637, 551))),
            //  row G
            96 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(174, 617))),
            97 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(241, 617))),
            98 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(306, 617))),
            99 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(373, 617))),
            100 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(439, 617))),
            101 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(505, 617))),
            102 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(571, 617))),
            103 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(637, 617))),
            //  row H
            112 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(174, 683))),
            113 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(241, 683))),
            114 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(306, 683))),
            115 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(373, 683))),
            116 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(439, 683))),
            117 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(505, 683))),
            118 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(571, 683))),
            119 to arrayOf(KeyDrawData(-1, -1, -1, -1, Point(637, 683))),
            // numbers
            104 to arrayOf(
                KeyDrawData(176, 105, 176, 98, Point(176, 93), color = Color.YELLOW),
                KeyDrawData(
                    760,
                    609,
                    790,
                    609,
                    Point(802, 614),
                    alignment = "Left",
                    color = Color.BLACK,
                    lineColor = Color.YELLOW
                )
            ),
            105 to arrayOf(KeyDrawData(242, 105, 242, 98, Point(242, 93), color = Color.YELLOW)),
            106 to arrayOf(KeyDrawData(307, 105, 307, 98, Point(307, 93), color = Color.YELLOW)),
            107 to arrayOf(KeyDrawData(374, 105, 374, 98, Point(374, 93), color = Color.YELLOW)),
            108 to arrayOf(KeyDrawData(439, 105, 439, 98, Point(439, 93), color = Color.YELLOW)),
            109 to arrayOf(KeyDrawData(505, 105, 505, 98, Point(505, 93), color = Color.YELLOW)),
            110 to arrayOf(KeyDrawData(572, 105, 572, 98, Point(572, 93), color = Color.YELLOW)),
            111 to arrayOf(KeyDrawData(637, 105, 637, 98, Point(637, 93), color = Color.YELLOW)),
            // letters
            8 to arrayOf(
                KeyDrawData(
                    760,
                    213,
                    790,
                    213,
                    Point(802, 218),
                    alignment = "Left",
                    color = Color.BLACK,
                    lineColor = Color.YELLOW
                )
            ),
            24 to arrayOf(
                KeyDrawData(
                    760,
                    279,
                    790,
                    279,
                    Point(802, 284),
                    alignment = "Left",
                    color = Color.BLACK,
                    lineColor = Color.YELLOW
                )
            ),
            40 to arrayOf(
                KeyDrawData(
                    760,
                    345,
                    790,
                    345,
                    Point(802, 350),
                    alignment = "Left",
                    color = Color.BLACK,
                    lineColor = Color.YELLOW
                )
            ),
            56 to arrayOf(
                KeyDrawData(
                    760,
                    410,
                    790,
                    410,
                    Point(802, 415),
                    alignment = "Left",
                    color = Color.BLACK,
                    lineColor = Color.YELLOW
                )
            ),
            72 to arrayOf(
                KeyDrawData(
                    760,
                    478,
                    790,
                    478,
                    Point(802, 483),
                    alignment = "Left",
                    color = Color.BLACK,
                    lineColor = Color.YELLOW
                )
            ),
            88 to arrayOf(
                KeyDrawData(
                    760,
                    543,
                    790,
                    543,
                    Point(802, 548),
                    alignment = "Left",
                    color = Color.BLACK,
                    lineColor = Color.YELLOW
                )
            ),
            120 to arrayOf(
                KeyDrawData(
                    760,
                    675,
                    790,
                    675,
                    Point(802, 680),
                    alignment = "Left",
                    color = Color.BLACK,
                    lineColor = Color.YELLOW
                )
            )
        )
    }

    override fun displaySize(): Dimension {
        return Dimension(880, 825)
    }

    override fun createImage(): Image {
        val img = ImageIO.read(javaClass.getResourceAsStream("/devices/launchpad-mini.jpg"))
        return img.getScaledInstance(800, 800, BufferedImage.SCALE_SMOOTH)
    }
}