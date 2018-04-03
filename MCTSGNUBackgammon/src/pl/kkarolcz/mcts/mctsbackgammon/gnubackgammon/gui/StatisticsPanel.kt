package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gui

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonGamesProgress.BackgammonMCTSProgressListener
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonGamesProgress.BackgammonMCTSProgressListenerAdapter
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gui.utils.DPIUtils
import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.SwingUtilities
import javax.swing.table.DefaultTableModel

/**
 * Created by kkarolcz on 25.03.2018.
 */
class StatisticsPanel : JPanel() {
    private val tableModel = StatisticsTableModel()
    private val table = JTable(tableModel)

    val progressListener: BackgammonMCTSProgressListener = StatisticsPanelBackgammonMCTSProgressListener()

    init {
        table.rowHeight = (DPIUtils.getScaleFactor() * 22).toInt()
        buildPanel()
    }

    private fun buildPanel() {
        layout = BorderLayout()
        add(JScrollPane(table), BorderLayout.CENTER)
    }


    private class StatisticsTableModel : DefaultTableModel(arrayOf("Game", "Rounds", "Winner"), 0) {
        fun addRow(game: Int, gameRounds: Int, winner: Player) {
            addRow(arrayOf(game, gameRounds, winner))
        }
    }

    private inner class StatisticsPanelBackgammonMCTSProgressListener : BackgammonMCTSProgressListenerAdapter() {

        override fun onGamesSequenceStart(gamesCount: Int, simulationsLimit: Int) {
            SwingUtilities.invokeLater {
                while (tableModel.rowCount > 0) {
                    tableModel.removeRow(0)
                }
            }
        }

        override fun onGameEnd(game: Int, gameRounds: Int, winner: Player) {
            SwingUtilities.invokeLater {
                tableModel.addRow(game, gameRounds, winner)
            }
        }

    }
}