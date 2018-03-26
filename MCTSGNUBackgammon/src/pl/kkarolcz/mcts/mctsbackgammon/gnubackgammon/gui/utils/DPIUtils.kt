package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gui.utils

import java.awt.Font
import java.awt.Toolkit
import javax.swing.UIDefaults
import javax.swing.UIManager

/**
 * Created by kkarolcz on 22.03.2018.
 */
object DPIUtils {

    private lateinit var lafDefaults: UIDefaults
    private val scaleFactor: Double = getScaleFactor()

    fun setDPIScaledLookAndFeel() {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        lafDefaults = UIManager.getLookAndFeelDefaults()
        setFontDefaultSizes()

    }

    fun getScaleFactor() = Toolkit.getDefaultToolkit().screenResolution.toDouble() / 96

    private fun setFontDefaultSizes() {
        val fontSize = scale(12)
        lafDefaults.keys
                .filterNotNull()
                .filter { key -> key.toString().toLowerCase().contains("font") }
                .forEach { key ->
                    var font: Font? = UIManager.getDefaults().getFont(key)
                    if (font != null) {
                        font = font.deriveFont(fontSize.toFloat())
                        UIManager.put(key, font)
                    }
                }
    }

    private fun scale(number: Int): Int = (number * scaleFactor).toInt()

}