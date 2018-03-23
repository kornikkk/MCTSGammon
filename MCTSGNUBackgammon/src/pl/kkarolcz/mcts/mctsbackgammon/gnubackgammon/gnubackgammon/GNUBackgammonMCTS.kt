package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon

import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonMCTS
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.mcts.convertToBackgammonState
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.mcts.formatForGNUBackgammon
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.server.BoardInfo
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.server.GNUBackgammonReceiver
import pl.kkarolcz.mcts.node.selectionpolicies.UCTNodeSelectionPolicy

/**
 * Created by kkarolcz on 29.08.2017.
 */
class GNUBackgammonMCTS : GNUBackgammonReceiver {
    private val backgammonMCTS = BackgammonMCTS(UCTNodeSelectionPolicy())

    fun reset(simulationsLimit: Int? = null) {
        backgammonMCTS.reset(simulationsLimit)
    }

    override fun onBoardInfoReceived(boardInfo: BoardInfo, callback: (String) -> Unit) {
        val backgammonState = boardInfo.convertToBackgammonState()
        when (backgammonMCTS.gameStarted) {
            false -> backgammonMCTS.newGame(backgammonState)
            true -> backgammonMCTS.nextRound(backgammonState)
        }

        playRound(callback)
    }

    private inline fun playRound(callback: (String) -> Unit) {
        val bestNode = backgammonMCTS.playRound()
        callback(formatForGNUBackgammon(bestNode.originMove))
    }
}