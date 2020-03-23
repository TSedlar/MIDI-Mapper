package me.sedlar.midi

import javafx.embed.swing.SwingFXUtils
import javafx.scene.Scene
import javafx.scene.image.ImageView
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.awt.*
import java.awt.image.BufferedImage

data class KeyDrawData(
    val lineX1: Int,
    val lineY1: Int,
    val lineX2: Int,
    val lineY2: Int,
    val labelPoint: Point,
    var alignment: String = "middle",
    val color: Color = Color.BLACK,
    val lineColor: Color = color,
    val outlineColor: Color = Color.BLACK,
    val lineOutlineColor: Color = Color.BLACK
)

abstract class MIDIDisplay(val deviceName: String) {

    private var _img: Image? = null

    val img: Image
        get() {
            if (_img == null) {
                _img = createImage()
            }
            return _img!!
        }

    abstract fun createMapping(): Map<Int, Array<KeyDrawData>>

    abstract fun displaySize(): Dimension

    abstract fun createImage(): Image

    open fun shortenLabel(string: String): String {
        return when (string) {
            "Left click" -> "L click"
            "Right click" -> "R click"
            "Middle click" -> "M click"
            else -> string
        }
    }

    open fun createOverlay(g: Graphics2D, profile: DeviceProfile) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        createMapping().forEach { (btn, dataArray) ->
            profile.bindings.find { it.btn == btn.toByte() }?.let { binding ->
                for (data in dataArray) {
                    g.color = data.lineOutlineColor
                    g.drawLine(data.lineX1 + 1, data.lineY1 + 1, data.lineX2 + 1, data.lineY2 + 1)
                    g.color = data.lineColor
                    g.drawLine(data.lineX1, data.lineY1, data.lineX2, data.lineY2)

                    var text = shortenLabel(binding.data)
                    if (text.length > 10) {
                        text = shortenLabel(binding.output)
                    }
                    var x = data.labelPoint.x
                    if (data.alignment == "middle") {
                        x -= (g.fontMetrics.stringWidth(text) / 2)
                    }

                    if (data.lineX1 != -1) {
                        g.color = data.outlineColor
                        if (data.color != data.outlineColor) {
                            g.drawString(text, x + 1, data.labelPoint.y)
                            g.drawString(text, x - 1, data.labelPoint.y)
                            g.drawString(text, x, data.labelPoint.y + 1)
                            g.drawString(text, x, data.labelPoint.y - 1)
                            g.drawString(text, x - 1, data.labelPoint.y - 1)
                            g.drawString(text, x + 1, data.labelPoint.y - 1)
                            g.drawString(text, x - 1, data.labelPoint.y + 1)
                        }
                        g.drawString(text, x + 1, data.labelPoint.y + 1)
                    }
                    g.color = data.color
                    g.drawString(text, x, data.labelPoint.y)
                }
            }
        }
    }

    fun showDialog(profile: DeviceProfile) {
        val dialog = Stage()
        dialog.isResizable = false

        val size = displaySize()

        val image = img

        val viewportImage = BufferedImage(
            size.width,
            size.height,
            BufferedImage.TYPE_INT_ARGB
        )

        val g = viewportImage.createGraphics()

        g.color = Color.WHITE
        g.fillRect(0, 0, size.width, size.height)

        g.drawImage(
            image,
            (size.width / 2) - (image.getWidth(null) / 2),
            (size.height / 2) - (image.getHeight(null) / 2),
            null
        )

        createOverlay(g, profile)

        g.dispose()

        val vbox = VBox()
        vbox.style = "-fx-background-color: white"

        vbox.children.add(ImageView(SwingFXUtils.toFXImage(viewportImage, null)))

        dialog.scene = Scene(vbox)
        dialog.show()
    }
}