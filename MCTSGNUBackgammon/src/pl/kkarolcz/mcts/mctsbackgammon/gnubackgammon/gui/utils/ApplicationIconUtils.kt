package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gui.utils

import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.swing.JFrame

/**
 * Created by kkarolcz on 23.03.2018.
 */
object ApplicationIconUtils {

    private val ICON16 = load("icon_16.png")
    private val ICON24 = load("icon_24.png")
    private val ICON32 = load("icon_32.png")
    private val ICON64 = load("icon_64.png")
    private val ICON128 = load("icon_128.png")
    private val ICON256 = load("icon_256.png")

    private val icons = listOf(ICON16, ICON24, ICON32, ICON64, ICON128, ICON256)

    fun setIcons(frame: JFrame) {
        frame.iconImages = icons.toList()
    }

    private fun load(name: String): BufferedImage = ImageIO.read(ApplicationIconUtils.javaClass.getResource(name))

}