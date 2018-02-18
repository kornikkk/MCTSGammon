package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import org.junit.Ignore
import org.junit.Test
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex.Companion.BAR_INDEX
import kotlin.test.fail

/**
 * Created by kkarolcz on 27.08.2017.
 */
class BackgammonDoublingMovesTest : AbstractBackgammonMovesTest() {

    @Test
    fun `Test single checker move`() {
        player1Checkers.put(24, 1)
        val movesSequence = movesSequence(
                move(24, 22),
                move(22, 20),
                move(20, 18),
                move(18, 16)
        )
        assertAllMovesFound(dice(2, 2), movesSequence)
    }

    @Test
    fun `Test multiple checkers move`() {
        player1Checkers.put(24, 1)
        player1Checkers.put(23, 1)
        val movesSequence = listOf(
                movesSequence(move(24, 22), move(22, 20), move(20, 18), move(18, 16)),
                movesSequence(move(24, 22), move(22, 20), move(20, 18), move(23, 21)),
                movesSequence(move(24, 22), move(22, 20), move(23, 21), move(21, 19)),
                movesSequence(move(24, 22), move(23, 21), move(21, 19), move(19, 17)),
                movesSequence(move(23, 21), move(21, 19), move(19, 17), move(17, 15)),
                movesSequence(move(23, 21), move(21, 19), move(19, 17), move(24, 22)),
                movesSequence(move(23, 21), move(21, 19), move(24, 22), move(22, 20)),
                movesSequence(move(23, 21), move(24, 22), move(22, 20), move(20, 18)),
                movesSequence(move(24, 22), move(22, 20), move(23, 21), move(20, 18)),
                movesSequence(move(24, 22), move(23, 21), move(21, 19), move(22, 20)),
                movesSequence(move(24, 22), move(23, 21), move(22, 20), move(20, 18)),
                movesSequence(move(24, 22), move(23, 21), move(22, 20), move(21, 19)),
                movesSequence(move(23, 21), move(24, 22), move(21, 19), move(19, 17)),
                movesSequence(move(23, 21), move(21, 19), move(24, 22), move(19, 17)),
                movesSequence(move(23, 21), move(24, 22), move(22, 20), move(21, 19)),
                movesSequence(move(23, 21), move(24, 22), move(21, 19), move(22, 20))
        )
        assertAllMovesFound(dice(2, 2), movesSequence)
    }

    @Test
    fun `Test one locked checker move`() {
        player1Checkers.put(24, 2)
        player2Checkers.put(toOpponentsIndex(18), 2)
        val movesSequence = listOf(
                movesSequence(move(24, 22), move(22, 20), move(24, 22), move(22, 20)),
                movesSequence(move(24, 22), move(24, 22), move(22, 20), move(22, 20))
        )
        assertAllMovesFound(dice(2, 2), movesSequence)
    }

    @Test
    fun `Test move from bar on an empty point and then locked`() {
        player1Checkers.put(BAR_INDEX, 1)
        player2Checkers.put(toOpponentsIndex(21), 2)
        val movesSequence = listOf(movesSequence(move(BAR_INDEX, 23)))
        assertAllMovesFound(dice(2, 2), movesSequence)
    }

    @Test
    fun `Test move from bar on an empty point and then further`() {
        fail("Not implemented yet")
    }

    @Test
    fun `Test hit move from bar on single opponent's checker on point`() {
        fail("Not implemented yet")
    }


    @Test
    fun `Test moves not possible from bar on 2 of opponent's checkers on point`() {
        fail("Not implemented yet")
    }

    @Test
    fun `Test hit move on single opponent's checker on point and then locked`() {
        fail("Not implemented yet")
    }

    @Test
    fun `Test many moves possible`() {
        fail("Not implemented yet")
    }

    @Ignore
    @Test
    fun testPerformance() {
        fail("Not implemented yet")
    }

    @Test
    fun `Test bear off move for new index just out of the board`() {
        fail("Not implemented yet")
    }

    //
//
    @Test
    fun `Test bear off move for new index farther out of the board`() {
        fail("Not implemented yet")
    }

    @Test
    fun `Test bear off move not possible because not all checkers are in the home board`() {
        fail("Not implemented yet")
    }

}