package pl.kkarolcz.mcts.mctsbackgammon

/**
 * Created by kkarolcz on 24.08.2017.
 */
class BackgammonBoard {
    // Player Index -> Board Index -> Number of checkers
    private val board: Array<Array<Int>> = Array(2) { Array(25) { 0 } }

    fun getPlayerCheckers(backgammonPlayer: BackgammonPlayer): Array<Int> {
        return board[backgammonPlayer.toInt()]
    }
}