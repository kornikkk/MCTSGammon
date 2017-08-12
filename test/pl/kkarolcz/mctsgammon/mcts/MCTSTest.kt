package pl.kkarolcz.mctsgammon.mcts

import org.junit.Test
import pl.kkarolcz.mctsgammon.utils.array2d
import pl.kkarolcz.mctsgammon.utils.deepCopy

/**
 * Created by kkarolcz on 10.08.2017.
 */
class MCTSTest {
    @Test
    fun `Test simple MCTS`() {
        val root = MCTSNode(TicTacToeState())
        for (i in 1..1000)
            root.monteCarloRound()
        root.wins
    }
}

private val WINNING_POSITIONS = arrayOf(
        arrayOf(Pair(0, 0), Pair(0, 1), Pair(0, 2)),
        arrayOf(Pair(1, 0), Pair(1, 1), Pair(1, 2)),
        arrayOf(Pair(2, 0), Pair(2, 1), Pair(2, 2)),
        arrayOf(Pair(0, 0), Pair(1, 0), Pair(2, 0)),
        arrayOf(Pair(0, 1), Pair(1, 1), Pair(2, 1)),
        arrayOf(Pair(0, 2), Pair(1, 2), Pair(2, 2)),
        arrayOf(Pair(0, 0), Pair(1, 1), Pair(2, 2)),
        arrayOf(Pair(0, 2), Pair(1, 1), Pair(2, 0))
)

private enum class CellState {
    NONE, X, O
}

private class TicTacToeMove(val x: Int, val y: Int) : Move {

}

private class TicTacToeState : State<TicTacToeMove> {
    private val board: Array<Array<CellState>>

    override val moves: MutableList<TicTacToeMove>

    override val result: Result?
        get() {
            for (winningPosition in WINNING_POSITIONS) {
                if (winningPosition.all { xy -> get(xy) == CellState.O })
                    return Result(mapOf(Pair(0, Result.PlayerResult.WIN), Pair(1, Result.PlayerResult.LOSE)))
                if (winningPosition.all { xy -> get(xy) == CellState.X })
                    return Result(mapOf(Pair(0, Result.PlayerResult.LOSE), Pair(1, Result.PlayerResult.WIN)))
            }
            return null
        }

    override var previousPlayerId: Int

    constructor() {
        board = array2d(3, 3) { CellState.NONE }
        this.moves = findPossibleMoves()
        this.previousPlayerId = 1
    }

    private constructor (other: TicTacToeState) {
        this.board = other.board.deepCopy()
        this.moves = findPossibleMoves()
        this.previousPlayerId = other.previousPlayerId
    }

    override fun clone(): TicTacToeState {
        return TicTacToeState(this)
    }

    private fun findPossibleMoves(): MutableList<TicTacToeMove> {
        val moves: MutableList<TicTacToeMove> = mutableListOf()
        board.mapIndexed { x, row ->
            row.mapIndexed { y, value ->
                if (value == CellState.NONE)
                    moves.add(TicTacToeMove(x, y))
            }
        }
        return moves
    }


    override fun doMove(move: TicTacToeMove) {
        val cellState = if (this.currentPlayerId == 0) CellState.O else CellState.X
        board[move.x][move.y] = cellState
        previousPlayerId = currentPlayerId
    }

    private fun get(xy: Pair<Int, Int>) = board[xy.first][xy.second]
}