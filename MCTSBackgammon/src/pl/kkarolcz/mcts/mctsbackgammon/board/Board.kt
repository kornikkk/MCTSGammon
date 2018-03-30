package pl.kkarolcz.mcts.mctsbackgammon.board

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.BAR_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.isOnBoard
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.toOpponentsIndex
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.SingleMove

/**
 * Created by kkarolcz on 24.08.2017.
 */
class Board : Cloneable {

    private val mctsBoard: PlayerBoard
    private val opponentsBoard: PlayerBoard

    companion object {
        const val SIZE: Byte = 25
    }

    constructor(mctsBoard: PlayerBoard, opponentsBoard: PlayerBoard) {
        this.mctsBoard = mctsBoard
        this.opponentsBoard = opponentsBoard
    }

    private constructor(other: Board) {
        mctsBoard = other.mctsBoard.clone()
        opponentsBoard = other.opponentsBoard.clone()
    }

    public override fun clone() = Board(this)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Board

        if (mctsBoard != other.mctsBoard) return false
        if (opponentsBoard != other.opponentsBoard) return false

        return true
    }

    override fun hashCode(): Int {
        var result = mctsBoard.hashCode()
        result = 31 * result + opponentsBoard.hashCode()
        return result
    }


    fun getPlayerBoard(player: Player) = when (player) {
        Player.MCTS -> mctsBoard
        Player.OPPONENT -> opponentsBoard
    }

    fun doSingleMove(player: Player, move: SingleMove) {
        val playerBoard = getPlayerBoard(player)
        playerBoard.move(move)
        hitOpponentIfOccupied(player.opponent(), move)
    }

    private fun hitOpponentIfOccupied(opponent: Player, move: SingleMove) {
        val opponentCheckers = getPlayerBoard(opponent)
        val opponentsIndex = toOpponentsIndex(move.newIndex)
        if (isOnBoard(move.newIndex) && opponentCheckers.isOccupied(opponentsIndex)) {
            val opponentsMove = SingleMove(opponentsIndex, BAR_INDEX)
            opponentCheckers.move(opponentsMove)
        }
    }

}