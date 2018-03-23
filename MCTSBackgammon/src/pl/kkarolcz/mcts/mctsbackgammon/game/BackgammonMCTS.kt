package pl.kkarolcz.mcts.mctsbackgammon.game

import pl.kkarolcz.mcts.mctsbackgammon.settings.Statistics
import pl.kkarolcz.mcts.node.selectionpolicies.NodeSelectionPolicy

/**
 * Created by kkarolcz on 21.03.2018.
 */
class BackgammonMCTS(private val selectionPolicy: NodeSelectionPolicy) {
    private var simulationsLimit: Int = 0
    private var currentNode: BackgammonNode? = null

    val gameStarted get() = currentNode != null

    fun reset(simulationsLimit: Int?) {
        this.currentNode = null
        if (simulationsLimit != null)
            this.simulationsLimit = simulationsLimit

        Statistics.newGame()
        Statistics.Game.newRound()
    }

    fun newGame(backgammonState: BackgammonState) {
        Statistics.newGame()
        Statistics.Game.newRound()

        currentNode = BackgammonNode.createRootNode(selectionPolicy, backgammonState)
    }

    /**
     * Call after opponent's move
     */
    fun nextRound(backgammonState: BackgammonState) {
        if (currentNode == null) throw IllegalStateException("Game not started")

        Statistics.Game.newRound()

        val existingNode = currentNode!!.findChildNode(backgammonState)
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
        if (currentNode == null) throw IllegalStateException("Game not started")
        if (simulationsLimit == 0) throw IllegalStateException("Simulations limit not set")

        for (i in 1..simulationsLimit)
            currentNode!!.monteCarloRound()

        currentNode = currentNode!!.getBestMove()
        Statistics.Game.Round.finishRound(currentNode!!.originMove!!)
        return currentNode!!
    }
}
