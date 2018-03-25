package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonMCTS
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonMCTSProgress
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonState
import pl.kkarolcz.mcts.node.selectionpolicies.UCTNodeSelectionPolicy

/**
 * Created by kkarolcz on 29.08.2017.
 */
class GNUBackgammonMCTS(private val gnuBackgammon: GNUBackgammon) {
    val progress: BackgammonMCTSProgress = BackgammonMCTSProgress()

    private val backgammonMCTS = BackgammonMCTS(UCTNodeSelectionPolicy(), progress)
    private var gamesLeft: Int = 0

    init {
        gnuBackgammon.addGameListener(GNUBackgammonMCTSGameListener())
    }

    fun startNewGamesSequence(gamesProperties: GamesProperties) {
        progress.reset(gamesProperties.numberOfGames)
        backgammonMCTS.reset(gamesProperties.simulationsLimit)

        gnuBackgammon.setDifficulty(gamesProperties.difficulty)

        gamesLeft = gamesProperties.numberOfGames
        startSingleGame()
    }

    private fun startSingleGame() {
        gamesLeft -= 1
        gnuBackgammon.newGame()
    }

    private fun onNextRound(backgammonState: BackgammonState) {
        when (backgammonMCTS.gameStarted) {
            false -> backgammonMCTS.newGame(backgammonState)
            true -> backgammonMCTS.newRound(backgammonState)
        }

        playRound()
    }

    private fun onGameFinished(winner: Player) {
        backgammonMCTS.endGame(winner)
        if (gamesLeft > 0)
            startSingleGame()
    }

    private fun playRound() {
        val bestNode = backgammonMCTS.playRound()
        gnuBackgammon.doMove(bestNode.originMove!!)
    }


    private inner class GNUBackgammonMCTSGameListener : GameListener {
        override fun onNextRound(backgammonState: BackgammonState) {
            this@GNUBackgammonMCTS.onNextRound(backgammonState)
        }

        override fun onGameFinished(winner: Player) {
            this@GNUBackgammonMCTS.onGameFinished(winner)
        }
    }
}