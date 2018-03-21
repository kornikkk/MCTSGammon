package pl.kkarolcz.mcts.mctsbackgammon.game

import pl.kkarolcz.mcts.mctsbackgammon.settings.Statistics
import pl.kkarolcz.mcts.node.selectionpolicies.NodeSelectionPolicy

/**
 * Created by kkarolcz on 21.03.2018.
 */
class BackgammonMCTS(private val selectionPolicy: NodeSelectionPolicy, private val simulationsLimit: Int) {
    private lateinit var currentNode: BackgammonNode

    val gameStarted = this::currentNode.isInitialized

    fun newGame(backgammonState: BackgammonState) {
        Statistics.newGame()
        currentNode = BackgammonNode.createRootNode(selectionPolicy, backgammonState)
    }

    /**
     * Call after opponent's move
     */
    fun nextRound(backgammonState: BackgammonState) {
        Statistics.currentGame.newRound() //TODO make non static?

        val existingNode = currentNode.findChildNode(backgammonState)
        currentNode = when (existingNode) {
            null -> BackgammonNode.createRootNode(selectionPolicy, backgammonState)
            else -> {
                val dice = backgammonState.dice ?: throw IllegalStateException("Dice has to be provided")
                existingNode.discardOtherDice(dice)
                existingNode
            }
        }
    }

    /**
     * Play the new round
     * @return Best node which should be played
     */
    fun playRound(): BackgammonNode {
        for (i in 1..simulationsLimit)
            currentNode.monteCarloRound()

        Statistics.logRound()

        currentNode = currentNode.getBestMove() //TODO set dice before and discard wrong moves
        return currentNode
    }
}
