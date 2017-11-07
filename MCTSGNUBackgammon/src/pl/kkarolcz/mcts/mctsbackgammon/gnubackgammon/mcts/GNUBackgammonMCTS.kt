package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.mcts

import pl.kkarolcz.mcts.MCTSNode
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.BackgammonMovesSequence
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.server.BoardInfo
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.server.GNUBackgammonReceiver
import pl.kkarolcz.mcts.node.selectionpolicies.UCTNodeSelectionPolicy

/**
 * Created by kkarolcz on 29.08.2017.
 */
class GNUBackgammonMCTS : GNUBackgammonReceiver {
    var gameStarted = false
    private lateinit var currentNode: MCTSNode<BackgammonMovesSequence>

    companion object {
        val SIMULATIONS_LIMIT = 10000
    }

    override fun onBoardInfoReceived(boardInfo: BoardInfo, response: (String) -> Unit) {
        val backgammonState = boardInfo.convertToBackgammonState()
        currentNode = when (gameStarted) {
            false -> {
                // New game
                gameStarted = true
                MCTSNode.createRootNode(UCTNodeSelectionPolicy(), backgammonState)
            }
            else -> {
                // Opponent's move
                currentNode.findOrCreateChildNode(backgammonState)
            }

        }
        playRound(response)
    }

    private fun playRound(callback: (String) -> Unit) {
        for (i in 1..SIMULATIONS_LIMIT)
            currentNode.monteCarloRound()

        currentNode = currentNode.bestMove
        callback(currentNode.origin!!.convertToGNUBackgammonMove())
    }
}