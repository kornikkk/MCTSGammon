package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gui.utils

import java.awt.Font
import java.awt.Toolkit
import javax.swing.UIManager

/**
 * Created by kkarolcz on 22.03.2018.
 */
object DPIUtils {

    fun setDPIScaledLookAndFeel() {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        getScaleFactor()
        setDefaultSize((12 * getScaleFactor()).toInt())

    }

    private fun setDefaultSize(size: Int) {
        UIManager.getLookAndFeelDefaults().keys
                .filterNotNull()
                .filter { key -> key.toString().toLowerCase().contains("font") }
                .forEach { key ->
                    var font: Font? = UIManager.getDefaults().getFont(key)
                    if (font != null) {
                        font = font.deriveFont(size.toFloat())
                        UIManager.put(key, font)
                    }
                }
    }

    private fun getScaleFactor() = Toolkit.getDefaultToolkit().screenResolution.toDouble() / 96
}