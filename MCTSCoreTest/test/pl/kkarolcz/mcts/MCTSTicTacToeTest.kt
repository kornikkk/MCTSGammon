package pl.kkarolcz.mcts

import org.junit.Test
import pl.kkarolcz.mcts.node.selectionpolicies.UCTNodeSelectionPolicy
import pl.kkarolcz.mctsgammon.utils.array2d
import pl.kkarolcz.mctsgammon.utils.deepCopy
import pl.kkarolcz.mctsgammon.utils.randomElement
import kotlin.test.assertTrue

/**
 * Created by kkarolcz on 10.08.2017.
 */
class MCTSTest {
    @Test
    fun `Test simple MCTS`() {
        val maxGames = 100

        var wins = 0
        var draws = 0
        for (games in 1..maxGames) {
            val root = MCTSNode.createRootNode(UCTNodeSelectionPolicy(), TicTacToeState())

            var node = root
            while (!node.isFullyExpanded) {
                for (i in 1..1000)
                    node.monteCarloRound()
                node = node.bestMove // do best move found by MCTS
                if (!node.isFullyExpanded)
                    node = node.getChildren().randomElement() // random opponent's move
            }
            if (node.result?.get(0) == Result.PlayerResult.WIN) ++wins
            else if (node.result == null) ++draws
            node.toString()
        }

        // Tic Tac Toe is pretty simple to lose with random moves. Let's say 70% of wins is enough for test to pass
        println("Wins: $wins")
        println("Draws: $draws")
        assertTrue { wins > 0.9 * maxGames }
    }
}

@Suppress("UNCHECKED_CAST")
fun MCTSNode<TicTacToeMove>.getState(): TicTacToeState {
    val state = javaClass.getDeclaredField("state")
    state.isAccessible = true
    return state.get(this) as TicTacToeState
}

@Suppress("UNCHECKED_CAST")
fun MCTSNode<TicTacToeMove>.getChildren(): List<MCTSNode<TicTacToeMove>> {
    val state = javaClass.getDeclaredField("children")
    state.isAccessible = true
    return state.get(this) as List<MCTSNode<TicTacToeMove>>
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
    NONE, X, O;

    override fun toString(): String = if (this == NONE) " " else super.toString()
}

data class TicTacToeMove(val row: Int, val column: Int) : Move

class TicTacToeState : MCTSState<TicTacToeMove> {
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
        this.previousPlayerId = 1
        this.moves = findPossibleMoves()
    }

    private constructor (other: TicTacToeState) {
        this.board = other.board.deepCopy()
        this.previousPlayerId = other.previousPlayerId
        this.moves = findPossibleMoves()
    }

    override fun clone(): MCTSState<TicTacToeMove> {
        return TicTacToeState(this)
    }

    override fun toString(): String {
        return super.toString() + "\n" +
                board.joinToString("\n") { row -> "|${row.joinToString("|")}|" }
    }

    private fun findPossibleMoves(): MutableList<TicTacToeMove> {
        val moves: MutableList<TicTacToeMove> = mutableListOf()
        if (result == null) {
            for ((rowIndex, row) in board.withIndex()) {
                for ((columnIndex, column) in row.withIndex()) {
                    if (column == CellState.NONE)
                        moves.add(TicTacToeMove(rowIndex, columnIndex))
                }
            }
        }
        return moves
    }


    override fun doMoveImpl(move: TicTacToeMove) {
        if (board[move.row][move.column] != CellState.NONE)
            throw IllegalStateException("Illegal move on non-empty board field")
        if (result == null)
            board[move.row][move.column] = if (currentPlayerId == 0) CellState.O else CellState.X
        else
            moves.clear()
    }

    private fun get(xy: Pair<Int, Int>) = board[xy.first][xy.second]
}