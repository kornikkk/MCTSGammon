package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon

import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gui.utils.DPIUtils
import pl.kkarolcz.mcts.mctsbackgammon.settings.Statistics
import java.io.File


/**
 * Created by kkarolcz on 29.08.2017.
 */

fun main(args: Array<String>) {
    DPIUtils.setDPIScaledLookAndFeel()
    Statistics.setLogFile(File("log.log"))
    Application.run()
}

