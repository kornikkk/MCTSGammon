package pl.kkarolcz.mcts.mctsbackgammon.game

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.node.selectionpolicies.NodeSelectionPolicy

/**
 * Created by kkarolcz on 21.03.2018.
 */
class BackgammonMCTS(private val selectionPolicy: NodeSelectionPolicy, private val progress: BackgammonMCTSProgress) {
    private var simulationsLimit: Int = 0
    private var currentNode: BackgammonNode? = null

    val gameStarted get() = currentNode != null

    fun reset(simulationsLimit: Int) {
        this.simulationsLimit = simulationsLimit
        currentNode = null
        progress.setSimulationsLimit(simulationsLimit)
    }

    fun endGame(winner: Player) {
        currentNode = null
        progress.endGame(winner)
    }

    fun newGame(backgammonState: BackgammonState) {
        currentNode = BackgammonNode.createRootNode(selectionPolicy, backgammonState)
        progress.newGame()
    }

    /**
     * Call after opponent's move
     */
    fun newRound(backgammonState: BackgammonState) {
        if (currentNode == null) throw IllegalStateException("Game not started")

        val existingNode = currentNode!!.findChildNode(backgammonState)
        currentNode = when (existingNode) {
            null -> BackgammonNode.createRootNode(selectionPolicy, backgammonState)
            else -> {
                val dice = backgammonState.dice ?: throw IllegalStateException("Dice has to be provided")
                existingNode.discardOtherDice(dice)
                existingNode
            }
        }

        progress.newGameRound()
    }

    /**
     * Play the new round
     * @return Best node which should be played
     */
    fun playRound(): BackgammonNode {
        if (currentNode == null) throw IllegalStateException("Game not started")
        if (simulationsLimit == 0) throw IllegalStateException("Simulations limit not set")

        for (i in 1..simulationsLimit) {
            progress.newMonteCarloRound()
            currentNode!!.monteCarloRound()
        }

        currentNode = currentNode!!.bestNode
        return currentNode!!
    }
}
