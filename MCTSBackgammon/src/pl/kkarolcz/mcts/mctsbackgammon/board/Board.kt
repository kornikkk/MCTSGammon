package pl.kkarolcz.mcts.mctsbackgammon.board

import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.BAR_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.isOnBoard
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.toOpponentsIndex
import pl.kkarolcz.mcts.mctsbackgammon.game.Player
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.SingleMove
import java.util.*

/**
 * Created by kkarolcz on 24.08.2017.
 */
class Board : Cloneable {

    private val player1Board: PlayerBoard
    private val player2Board: PlayerBoard
    private var doneMoves: Stack<DoneMove>? = null

    companion object {
        val SIZE: Byte = 25
    }

    constructor(player1Board: PlayerBoard, player2Board: PlayerBoard) {
        this.player1Board = player1Board
        this.player2Board = player2Board
    }

    private constructor(other: Board) {
        player1Board = other.player1Board.clone()
        player2Board = other.player2Board.clone()
    }

    public override fun clone() = Board(this)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Board

        if (player1Board != other.player1Board) return false
        if (player2Board != other.player2Board) return false

        return true
    }

    override fun hashCode(): Int {
        var result = player1Board.hashCode()
        result = 31 * result + player2Board.hashCode()
        return result
    }


    fun getPlayerCheckers(player: Player) = when (player) {
        Player.PLAYER_ONE -> player1Board
        Player.PLAYER_TWO -> player2Board
    }

    // TODO Simplify if undo is removed
    fun doMove(player: Player, move: SingleMove) {
        val playerCheckers = getPlayerCheckers(player)
        playerCheckers.move(move)

        val opponentMove: SingleMove? = hitOpponentIfOccupied(player.opponent(), move)

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

    private fun hitOpponentIfOccupied(opponent: Player, move: SingleMove): SingleMove? {
        val opponentCheckers = getPlayerCheckers(opponent)
        val opponentsIndex = toOpponentsIndex(move.newIndex)
        if (isOnBoard(move.newIndex) && opponentCheckers.isOccupied(opponentsIndex)) {
            val opponentsMove = SingleMove.create(opponentsIndex, BAR_INDEX)
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

    private class DoneMove(val player: Player, val playerMove: SingleMove, val opponentsMove: SingleMove?)

}