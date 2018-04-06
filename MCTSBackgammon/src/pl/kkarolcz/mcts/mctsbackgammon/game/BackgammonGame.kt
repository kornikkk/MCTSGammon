package pl.kkarolcz.mcts.mctsbackgammon.game

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.game.ai.*

/**
 * Created by kkarolcz on 21.03.2018.
 */
class BackgammonGame(private val progress: BackgammonGamesProgress) {
    private var simulationsLimit: Int = 0
    private var currentNode: BackgammonNode? = null
    private var backgammonAI: BackgammonAI? = null

    val gameStarted get() = currentNode != null

    fun reset(simulationsLimit: Int, backgammonAIType: BackgammonAIType) {
        this.simulationsLimit = simulationsLimit
        this.currentNode = null
        this.backgammonAI = backgammonAIType.create(progress)
    }

    fun endGame(winner: Player) {
        currentNode = null
        progress.endGame(winner)
    }

    fun newGame(backgammonState: BackgammonState) {
        if (backgammonAI == null) throw IllegalStateException("Backgammon AI not set")

        currentNode = backgammonAI?.createRootNode(backgammonState)
        progress.newGame()
        progress.newGameRound(backgammonState.dice!!)
    }

    /**
     * Call after opponent's move
     */
    fun newRound(backgammonState: BackgammonState) {
        if (currentNode == null) throw IllegalStateException("Game not started")
        if (backgammonAI == null) throw IllegalStateException("Backgammon AI not set")

        val existingNode = currentNode!!.findChildNode(backgammonState)
        currentNode = when (existingNode) {
            null -> backgammonAI?.createRootNode(backgammonState)
            else -> {
                val dice = backgammonState.dice ?: throw IllegalStateException("Dice has to be provided")
                existingNode.parent = null
                existingNode.discardOtherDice(dice)
                existingNode
            }
        }

        progress.newGameRound(backgammonState.dice!!)
    }

    /**
     * Play the new round
     * @return Best node which should be played
     */
    fun playRound(): BackgammonNode {
        if (currentNode == null) throw IllegalStateException("Game not started")
        if (simulationsLimit == 0) throw IllegalStateException("Simulations limit not set")
        if (backgammonAI == null) throw IllegalStateException("Backgammon AI not set")

        backgammonAI?.simulate(currentNode!!, simulationsLimit)

        currentNode = currentNode!!.bestNode
        progress.endGameRound(currentNode!!.originMove!!)
        return currentNode!!
    }
}
