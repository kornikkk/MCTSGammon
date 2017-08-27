package pl.kkarolcz.mcts.mctsbackgammon.board

import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonPlayer

/**
 * Created by kkarolcz on 24.08.2017.
 */
class BackgammonBoard : Cloneable {
    private val board: Array<BackgammonPlayerCheckers>

    companion object {
        val SIZE = BackgammonBoardIndex.MAX_INDEX + 1
    }

    constructor(player1Checkers: BackgammonPlayerCheckers, player2Checkers: BackgammonPlayerCheckers) {
        board = arrayOf(player1Checkers, player2Checkers)
    }

    constructor() {
        board = Array(2) { BackgammonPlayerCheckers() }
    }

    @Suppress("UNCHECKED_CAST")
    private constructor(other: BackgammonBoard) {
        val clonedBoard: Array<BackgammonPlayerCheckers?> = Array(2) { null }
        other.board.forEachIndexed { playerIndex, playerCheckers -> clonedBoard[playerIndex] = playerCheckers.clone() }
        this.board = clonedBoard as Array<BackgammonPlayerCheckers>
    }

    public override fun clone(): BackgammonBoard {
        return BackgammonBoard(this)
    }

    fun getPlayerCheckers(backgammonPlayer: BackgammonPlayer): BackgammonPlayerCheckers {
        return board[backgammonPlayer.toInt()]
    }

}