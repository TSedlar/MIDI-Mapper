package me.sedlar.midi.display

import me.sedlar.midi.DeviceProfile
import me.sedlar.midi.MIDIDisplay
import java.awt.*
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

private data class KeyDrawData(
    val lineX1: Int,
    val lineY1: Int,
    val lineX2: Int,
    val lineY2: Int,
    val labelPoint: Point,
    val color: Color = Color.BLACK
)

class LaunchkeyMiniDisplay : MIDIDisplay("Launchkey Mini") {

    private val btnMap = mapOf(
        // White keys (48, 50, 52, 53, 55, 57, 59, 60, 62, 64, 65, 67, 69, 71, 72)
        48 to KeyDrawData(145, 518, 145, 573, Point(145, 590)),
        50 to KeyDrawData(192, 518, 192, 558, Point(192, 575)),
        52 to KeyDrawData(239, 518, 239, 573, Point(239, 590)),
        53 to KeyDrawData(286, 518, 286, 558, Point(286, 575)),
        55 to KeyDrawData(332, 518, 332, 573, Point(332, 590)),
        57 to KeyDrawData(378, 518, 378, 558, Point(378, 575)),
        59 to KeyDrawData(424, 518, 424, 573, Point(424, 590)),
        60 to KeyDrawData(470, 518, 470, 558, Point(470, 575)),
        62 to KeyDrawData(516, 518, 516, 573, Point(516, 590)),
        64 to KeyDrawData(562, 518, 562, 558, Point(562, 575)),
        65 to KeyDrawData(608, 518, 608, 573, Point(608, 590)),
        67 to KeyDrawData(654, 518, 654, 558, Point(654, 575)),
        69 to KeyDrawData(700, 518, 700, 573, Point(700, 590)),
        71 to KeyDrawData(746, 518, 746, 558, Point(746, 575)),
        72 to KeyDrawData(792, 518, 792, 573, Point(792, 590)),
        // Black keys (49, 51, 54, 56, 58, 61, 63, 66, 68, 70)
        49 to KeyDrawData(166, 339, 166, 370, Point(166, 333), Color.YELLOW),
        51 to KeyDrawData(220, 339, 220, 370, Point(220, 333), Color.YELLOW),
        54 to KeyDrawData(303, 339, 303, 370, Point(303, 333), Color.YELLOW),
        56 to KeyDrawData(355, 339, 355, 370, Point(355, 333), Color.YELLOW),
        58 to KeyDrawData(407, 339, 407, 370, Point(407, 333), Color.YELLOW),
        61 to KeyDrawData(490, 339, 490, 370, Point(490, 333), Color.YELLOW),
        63 to KeyDrawData(545, 339, 545, 370, Point(545, 333), Color.YELLOW),
        66 to KeyDrawData(628, 339, 628, 370, Point(628, 333), Color.YELLOW),
        68 to KeyDrawData(680, 339, 680, 370, Point(680, 333), Color.YELLOW),
        70 to KeyDrawData(733, 339, 733, 370, Point(733, 333), Color.YELLOW),
        // Knob (21-28)
        21 to KeyDrawData(297, 125, 297, 142, Point(297, 118), Color.RED),
        22 to KeyDrawData(351, 125, 351, 142, Point(351, 118), Color.RED),
        23 to KeyDrawData(404, 125, 404, 142, Point(404, 118), Color.RED),
        24 to KeyDrawData(457, 125, 457, 142, Point(457, 118), Color.RED),
        25 to KeyDrawData(511, 125, 511, 142, Point(511, 118), Color.RED),
        26 to KeyDrawData(565, 125, 565, 142, Point(565, 118), Color.RED),
        27 to KeyDrawData(618, 125, 618, 142, Point(618, 118), Color.RED),
        28 to KeyDrawData(672, 125, 672, 142, Point(672, 118), Color.RED),
        // Pads (40-43, 48-51, 36-39, 44-47)
        40 to KeyDrawData(-1, -1, -1, -1, Point(297, 232)),
        41 to KeyDrawData(-1, -1, -1, -1, Point(352, 232)),
        42 to KeyDrawData(-1, -1, -1, -1, Point(405, 232)),
        43 to KeyDrawData(-1, -1, -1, -1, Point(458, 232)),
//        48 to KeyDrawData(-1, -1, -1, -1, Point(511, 232)),
//        49 to KeyDrawData(-1, -1, -1, -1, Point(565, 232)),
//        50 to KeyDrawData(-1, -1, -1, -1, Point(618, 232)),
//        51 to KeyDrawData(-1, -1, -1, -1, Point(672, 232)),
        36 to KeyDrawData(-1, -1, -1, -1, Point(297, 286)),
        37 to KeyDrawData(-1, -1, -1, -1, Point(352, 286)),
        38 to KeyDrawData(-1, -1, -1, -1, Point(405, 286)),
        39 to KeyDrawData(-1, -1, -1, -1, Point(458, 286)),
        44 to KeyDrawData(-1, -1, -1, -1, Point(511, 286)),
        45 to KeyDrawData(-1, -1, -1, -1, Point(565, 286)),
        46 to KeyDrawData(-1, -1, -1, -1, Point(618, 286)),
        47 to KeyDrawData(-1, -1, -1, -1, Point(672, 286)),
        // Right circles
        108 to KeyDrawData(724, 206, 724, 216, Point(724, 200), Color.ORANGE),
        109 to KeyDrawData(732, 290, 749, 308, Point(766, 320), Color.ORANGE),
        // Scene
        104 to KeyDrawData(787, 224, 787, 230, Point(787, 219), Color.ORANGE),
        105 to KeyDrawData(787, 279, 787, 285, Point(787, 300), Color.ORANGE),
        // Track
        106 to KeyDrawData(144, 214, 144, 220, Point(144, 208), Color.ORANGE),
        107 to KeyDrawData(188, 214, 188, 220, Point(188, 208), Color.ORANGE)
    )

    override fun displaySize(): Dimension {
        return Dimension(950, 650)
    }

    override fun createImage(): Image {
        val img = ImageIO.read(javaClass.getResourceAsStream("/devices/launchkey-mini.jpg"))
        return img.getScaledInstance(875, 583, BufferedImage.SCALE_SMOOTH)
    }

    override fun createOverlay(g: Graphics2D, profile: DeviceProfile) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        btnMap.forEach { (btn, data) ->
            profile.bindings.find { it.btn == btn.toByte() }?.let { binding ->
                g.color = data.color
                g.drawLine(data.lineX1, data.lineY1, data.lineX2, data.lineY2)
                val text = binding.data
                g.drawString(text, data.labelPoint.x - (g.fontMetrics.stringWidth(text) / 2), data.labelPoint.y)
            }
        }
    }
}