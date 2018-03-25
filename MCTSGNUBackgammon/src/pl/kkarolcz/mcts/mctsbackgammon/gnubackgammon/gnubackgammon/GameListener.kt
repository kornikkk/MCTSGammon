package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonState

/**
 * Created by kkarolcz on 25.03.2018.
 */
interface GameListener {
    fun onNextRound(backgammonState: BackgammonState)
    fun onGameFinished(winner: Player)

}