package pl.kkarolcz.mcts.mctsbackgammon.board

import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex.Companion.BAR_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex.Companion.isOnBoard
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex.Companion.toOpponentsIndex
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonPlayer
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.BackgammonMove
import java.util.*

/**
 * Created by kkarolcz on 24.08.2017.
 */
class BackgammonBoard : Cloneable {

    private val player1Checkers: BackgammonPlayerCheckers
    private val player2Checkers: BackgammonPlayerCheckers
    private var doneMoves: Stack<DoneMove>? = null

    companion object {
        val SIZE: Byte = 25
    }

    constructor(player1Checkers: BackgammonPlayerCheckers, player2Checkers: BackgammonPlayerCheckers) {
        this.player1Checkers = player1Checkers
        this.player2Checkers = player2Checkers
    }

    private constructor(other: BackgammonBoard) {
        player1Checkers = other.player1Checkers.clone()
        player2Checkers = other.player2Checkers.clone()
    }

    public override fun clone() = BackgammonBoard(this)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BackgammonBoard

        if (player1Checkers != other.player1Checkers) return false
        if (player2Checkers != other.player2Checkers) return false

        return true
    }

    override fun hashCode(): Int {
        var result = player1Checkers.hashCode()
        result = 31 * result + player2Checkers.hashCode()
        return result
    }


    fun getPlayerCheckers(player: BackgammonPlayer) = when (player) {
        BackgammonPlayer.PLAYER_ONE -> player1Checkers
        BackgammonPlayer.PLAYER_TWO -> player2Checkers
    }

    // TODO Simplify if undo is removed
    fun doMove(player: BackgammonPlayer, move: BackgammonMove) {
        val playerCheckers = getPlayerCheckers(player)
        playerCheckers.move(move)

        val opponentMove: BackgammonMove? = hitOpponentIfOccupied(player.opponent(), move)

        addDoneMove(DoneMove(player, move, opponentMove))
    }

    //TODO Remove in the future?
    fun undoLastMove() {
        val doneMove = popDoneMove()
        val player = doneMove.player

        val playerCheckers = getPlayerCheckers(player)
        val opponentCheckers = getPlayerCheckers(player.opponent())

        playerCheckers.move(doneMove.playerMove.reversed())
        if (doneMove.opponentsMove != null)
            opponentCheckers.move(doneMove.opponentsMove.reversed())
    }

    private fun hitOpponentIfOccupied(opponent: BackgammonPlayer, move: BackgammonMove): BackgammonMove? {
        val opponentCheckers = getPlayerCheckers(opponent)
        val opponentsIndex = toOpponentsIndex(move.newIndex)
        if (isOnBoard(move.newIndex) && opponentCheckers.isOccupied(opponentsIndex)) {
            val opponentsMove = BackgammonMove(opponentsIndex, BAR_INDEX)
            opponentCheckers.move(opponentsMove)
            return opponentsMove
        }
        return null
    }

    private fun addDoneMove(doneMove: DoneMove) {
        if (doneMoves == null)
            doneMoves = Stack()
        doneMoves!!.add(doneMove)
    }

    private fun popDoneMove(): DoneMove {
        return doneMoves?.pop() ?: throw IllegalStateException("No moves to undo")
    }

    private class DoneMove(val player: BackgammonPlayer, val playerMove: BackgammonMove, val opponentsMove: BackgammonMove?)

}