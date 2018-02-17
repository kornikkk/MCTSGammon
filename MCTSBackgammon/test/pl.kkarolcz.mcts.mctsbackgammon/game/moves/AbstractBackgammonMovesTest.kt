package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import org.junit.Before
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoard
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonPlayerCheckers
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonPlayer
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.BackgammonDice
import kotlin.test.assertEquals
import kotlin.test.fail

/**
 * Created by kkarolcz on 11.02.2018.
 */
open class AbstractBackgammonMovesTest {

    protected lateinit var player1Checkers: BackgammonPlayerCheckers
    protected lateinit var player2Checkers: BackgammonPlayerCheckers
    private lateinit var board: BackgammonBoard

    @Before
    fun initializeEmptyPlayerCheckersArrays() {
        player1Checkers = BackgammonPlayerCheckers()
        player2Checkers = BackgammonPlayerCheckers()
        player2Checkers.put(BackgammonBoardIndex.BAR_INDEX, 1)
        board = BackgammonBoard(player1Checkers, player2Checkers)
    }


    protected fun assertNoMovesFound(dice: BackgammonDice) {
        val searcher = FullMovesSearchNonDoubling(board, BackgammonPlayer.PLAYER_ONE, dice)
        assertEquals(emptyList(), searcher.findAll())
    }

    protected fun assertAllMovesFound(dice: BackgammonDice, vararg expectedMoves: BackgammonMovesSequence) {
        assertAllMovesFound(dice, expectedMoves.toList())
    }

    protected fun assertAllMovesFound(dice: BackgammonDice, expectedMoves: Iterable<BackgammonMovesSequence>) {
        val searcher = when (dice.doubling) {
            true -> FullMovesSearchDoubling(board, BackgammonPlayer.PLAYER_ONE, dice)
            false -> FullMovesSearchNonDoubling(board, BackgammonPlayer.PLAYER_ONE, dice)
        }
        val expectedSet = expectedMoves.toMutableSet()
        val actualSet = searcher.findAll().toMutableSet()
        if (expectedSet != actualSet && !actualSet.containsAll(expectedSet)) {
            expectedSet.removeAll(actualSet)
            fail("Not found moves: $expectedSet")
        }
        if (expectedSet != actualSet && !expectedSet.containsAll(actualSet)) {
            actualSet.removeAll(expectedMoves)
            fail("Not expected moves: $actualSet")
        }
    }

    protected fun dice(firstDice: Number, secondDice: Number) = BackgammonDice(firstDice.toByte(), secondDice.toByte())

    protected fun move(oldIndex: Number, newIndex: Number) = BackgammonMove.create(oldIndex.toByte(), newIndex.toByte())

    protected fun movesSequence(vararg moves: BackgammonMove) = BackgammonMovesSequence(moves.toList())

    protected fun toOpponentsIndex(index: Number) = BackgammonBoardIndex.toOpponentsIndex(index.toByte())
}