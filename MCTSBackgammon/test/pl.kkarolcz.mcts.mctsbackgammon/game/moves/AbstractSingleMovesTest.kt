package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import org.junit.Before
import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.board.Board
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex
import pl.kkarolcz.mcts.mctsbackgammon.board.PlayerBoard
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import kotlin.test.assertEquals
import kotlin.test.fail

/**
 * Created by kkarolcz on 11.02.2018.
 */
open class AbstractSingleMovesTest {

    protected lateinit var player1Board: PlayerBoard
    protected lateinit var player2Board: PlayerBoard
    private lateinit var board: Board

    @Before
    fun initializeEmptyPlayerCheckersArrays() {
        player1Board = PlayerBoard()
        player2Board = PlayerBoard()
        player2Board.put(BoardIndex.BAR_INDEX, 1)
        board = Board(player1Board, player2Board)
    }


    protected fun assertNoMovesFound(dice: Dice) {
        val searcher = FullMovesSearchNonDoubling(board, Player.MCTS, dice)
        assertEquals(emptyList(), searcher.findAll().toList())
    }

    protected fun assertAllMovesFound(dice: Dice, vararg expectedMoves: FullMove) {
        assertAllMovesFound(dice, expectedMoves.toList())
    }

    protected fun assertAllMovesFound(dice: Dice, expectedMoves: Iterable<FullMove>) {
        val expectedSet = expectedMoves.toMutableSet()
        val actualSet = searcher(dice).findAll().toMutableSet()
        if (expectedSet != actualSet && !actualSet.containsAll(expectedSet)) {
            expectedSet.removeAll(actualSet)
            fail("Not found untriedMoves: $expectedSet")
        }
        if (expectedSet != actualSet && !expectedSet.containsAll(actualSet)) {
            actualSet.removeAll(expectedMoves)
            fail("Not expected untriedMoves: $actualSet")
        }
    }

    protected fun searcher(dice: Dice) = when (dice.doubling) {
        true -> FullMovesSearchDoubling(board, Player.MCTS, dice)
        false -> FullMovesSearchNonDoubling(board, Player.MCTS, dice)
    }

    protected fun dice(firstDice: Number, secondDice: Number) = Dice(firstDice.toByte(), secondDice.toByte())

    protected fun move(oldIndex: Number, newIndex: Number) = SingleMove.create(oldIndex.toByte(), newIndex.toByte())

    protected fun movesSequence(vararg moves: SingleMove) = FullMove.create(*moves)

    protected fun toOpponentsIndex(index: Number) = BoardIndex.toOpponentsIndex(index.toByte())
}