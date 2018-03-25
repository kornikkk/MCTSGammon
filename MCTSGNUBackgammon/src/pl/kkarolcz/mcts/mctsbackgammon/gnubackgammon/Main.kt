package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon

import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gui.utils.DPIUtils


/**
 * Created by kkarolcz on 29.08.2017.
 */

fun main(args: Array<String>) {
    DPIUtils.setDPIScaledLookAndFeel()
    // StatisticsLogger.setLogFile("log.log")
    Application.run()
}

