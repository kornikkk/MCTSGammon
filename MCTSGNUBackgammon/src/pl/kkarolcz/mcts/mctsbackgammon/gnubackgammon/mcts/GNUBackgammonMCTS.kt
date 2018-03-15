package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.mcts

import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonNode
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.mctsbackgammon.game.statistics.Statistics
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.server.BoardInfo
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.server.GNUBackgammonReceiver
import pl.kkarolcz.mcts.node.selectionpolicies.UCTNodeSelectionPolicy

/**
 * Created by kkarolcz on 29.08.2017.
 */
class GNUBackgammonMCTS : GNUBackgammonReceiver {
    private var gameStarted = false
    private lateinit var currentNode: BackgammonNode

    companion object {
        val SIMULATIONS_LIMIT = 10_000
    }

    override fun onBoardInfoReceived(boardInfo: BoardInfo, callback: (String) -> Unit) {
        val backgammonState = boardInfo.convertToBackgammonState()
        currentNode = when (gameStarted) {
            false -> {
                Statistics.newGame()
                // New game
                gameStarted = true //TODO that doesn't reset when a new game is started
                BackgammonNode.createRootNode(UCTNodeSelectionPolicy(), backgammonState)
            }
            else -> {
                // Opponent's move
                val existingNode = currentNode.findChildNode(backgammonState)
                when (existingNode) {
                    null -> BackgammonNode.createRootNode(UCTNodeSelectionPolicy(), backgammonState)
                    else -> existingNode
                }
            }

        }
        playRound(backgammonState.dice!!, callback)
    }

    private inline fun playRound(dice: Dice, callback: (String) -> Unit) {
        Statistics.currentGame.newRound()

        for (i in 1..SIMULATIONS_LIMIT)
            currentNode.monteCarloRound()

        // GraphGenerator.drawGraph(GraphGenerator.generateGraph(currentNode))
        Statistics.logCurrentRound()

        currentNode = currentNode.getBestMove(dice) //TODO set dice before and discard wrong moves
        callback(convertToGNUBackgammonMove(currentNode.originMove))
    }
}