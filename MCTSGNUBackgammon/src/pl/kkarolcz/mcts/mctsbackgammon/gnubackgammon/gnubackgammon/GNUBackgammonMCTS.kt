package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonGame
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonGamesProgress
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonGamesProgress.BackgammonMCTSProgressListener
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonState
import pl.kkarolcz.mcts.node.selectionpolicies.UCTNodeSelectionPolicy

/**
 * Created by kkarolcz on 29.08.2017.
 */
class GNUBackgammonMCTS(private val gnuBackgammon: GNUBackgammon) {
    private val progress: BackgammonGamesProgress = BackgammonGamesProgress()
    private var gamesCount: Int = 0
    private var gamesLeft: Int = 0

    private val backgammonMCTS = BackgammonGame(progress)

    init {
        gnuBackgammon.addGameListener(GNUBackgammonMCTSGameListener())
    }

    fun addProgressListener(listener: BackgammonMCTSProgressListener) {
        progress.addProgressListener(listener)
    }

    fun startNewGamesSequence(gamesProperties: GamesProperties) {
        progress.newGamesSequence(gamesProperties.numberOfGames, gamesProperties.simulationsLimit)
        backgammonMCTS.reset(gamesProperties.simulationsLimit)

        gnuBackgammon.setDifficulty(gamesProperties.difficulty)

        gamesCount = gamesProperties.numberOfGames
        gamesLeft = gamesCount
        startSingleGame()
    }

    private fun startSingleGame() {
        gamesLeft -= 1
        gnuBackgammon.newGame()
    }

    private fun playRound() {
        val bestNode = backgammonMCTS.playRound()
        gnuBackgammon.doMove(bestNode.originMove!!)
    }


    private inner class GNUBackgammonMCTSGameListener : GameListener {

        override fun onNextRound(backgammonState: BackgammonState) {
            when (backgammonMCTS.gameStarted) {
                false -> backgammonMCTS.newGame(backgammonState)
                true -> backgammonMCTS.newRound(backgammonState)
            }

            playRound()
        }

        override fun onGameFinished(winner: Player) {
            backgammonMCTS.endGame(winner)
            when {
                gamesLeft > 0 -> startSingleGame()
                else -> progress.endGamesSequence(gamesCount)
            }
        }

    }

}