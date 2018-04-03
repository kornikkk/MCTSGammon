package pl.kkarolcz.mcts.mctsbackgammon.game

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.game.mcts.*

/**
 * Created by kkarolcz on 21.03.2018.
 */
class BackgammonGame(private val progress: BackgammonGamesProgress) {
    private var simulationsLimit: Int = 0
    private var currentNode: BackgammonNode? = null
        private var backgammonMCTS: BackgammonMCTS = SequentialHalvingV3(progress)
//    private var backgammonMCTS: BackgammonMCTS = SequentialHalvingV2(progress)
//    private var backgammonMCTS: BackgammonMCTS = SequentialHalvingV1(progress)
//    private var backgammonMCTS: BackgammonMCTS = ClassicMCTSWithUCT(progress)

    val gameStarted get() = currentNode != null

    fun reset(simulationsLimit: Int) {
        this.simulationsLimit = simulationsLimit
        currentNode = null
    }

    fun endGame(winner: Player) {
        currentNode = null
        progress.endGame(winner)
    }

    fun newGame(backgammonState: BackgammonState) {
        currentNode = backgammonMCTS.createRootNode(backgammonState)
        progress.newGame()
        progress.newGameRound(backgammonState.dice!!)
    }

    /**
     * Call after opponent's move
     */
    fun newRound(backgammonState: BackgammonState) {
        if (currentNode == null) throw IllegalStateException("Game not started")

        val existingNode = currentNode!!.findChildNode(backgammonState)
        currentNode = when (existingNode) {
            null -> backgammonMCTS.createRootNode(backgammonState)
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

        backgammonMCTS.simulate(currentNode!!, simulationsLimit)

        currentNode = currentNode!!.bestNode
        progress.endGameRound(currentNode!!.originMove!!)
        return currentNode!!
    }
}
