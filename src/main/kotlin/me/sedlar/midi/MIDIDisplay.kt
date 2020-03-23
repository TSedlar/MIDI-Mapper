package me.sedlar.midi

import javafx.embed.swing.SwingFXUtils
import javafx.scene.Scene
import javafx.scene.image.ImageView
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.Image
import java.awt.image.BufferedImage

abstract class MIDIDisplay(val deviceName: String) {

    private var _img: Image? = null

    val img: Image
        get() {
            if (_img == null) {
                _img = createImage()
            }
            return _img!!
        }

    abstract fun displaySize(): Dimension

    abstract fun createImage(): Image

    abstract fun createOverlay(g: Graphics2D, profile: DeviceProfile)

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