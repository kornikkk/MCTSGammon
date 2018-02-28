package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import org.junit.Ignore
import org.junit.Test
import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.board.Board
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.BAR_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.PlayerBoard
import java.util.*

/**
 * Created by kkarolcz on 27.08.2017.
 */
class BackgammonDoublingMovesTest : AbstractSingleMovesTest() {


    @Ignore
    @Test
    fun testPerformance() {
        val storeMoves = false
        val allMoves = mutableListOf<FullMove>()
        val random = Random()
        for (i in 1..10_000) {
            val player1Checkers = PlayerBoard()
            val player1RandomCheckers = ByteArray(26)

            val player2Checkers = PlayerBoard()
            val player2RandomCheckers = ByteArray(26)

            for (j in 1..15) {
                player1RandomCheckers[random.nextInt(26)] = 1
                player2RandomCheckers[random.nextInt(26)] = 1
            }
            for (j in 0..25) {
                val pointIndex = j.toByte()
                player1Checkers.put(pointIndex, player1RandomCheckers[j])
                player2Checkers.put(pointIndex, player2RandomCheckers[j])
            }

            val board = Board(player1Checkers, player2Checkers)
            val die = 1 + random.nextInt(6)
            val dices = dice(die, die)
            val moves = FullMovesSearchDoubling(board, Player.MCTS, dices).findAll()

            if (storeMoves)
                allMoves.addAll(moves)
        }
        return
    }

    @Test
    fun `Test single checker move`() {
        player1Board.put(24, 1)
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
        player1Board.put(24, 1)
        player1Board.put(23, 1)
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
        player1Board.put(24, 2)
        player2Board.put(toOpponentsIndex(18), 2)
        val movesSequence = listOf(
                movesSequence(move(24, 22), move(22, 20), move(24, 22), move(22, 20)),
                movesSequence(move(24, 22), move(24, 22), move(22, 20), move(22, 20))
        )
        assertAllMovesFound(dice(2, 2), movesSequence)
    }

    @Test
    fun `Test move from bar on an empty point and then locked`() {
        player1Board.put(BAR_INDEX, 1)
        player2Board.put(toOpponentsIndex(21), 2)
        val movesSequence = listOf(movesSequence(move(BAR_INDEX, 23)))
        assertAllMovesFound(dice(2, 2), movesSequence)
    }

    @Test
    fun `Test 2 moves from bar on an empty point and then locked`() {
        player1Board.put(BAR_INDEX, 2)
        player2Board.put(toOpponentsIndex(21), 2)
        val movesSequence = listOf(movesSequence(move(BAR_INDEX, 23), move(BAR_INDEX, 23)))
        assertAllMovesFound(dice(2, 2), movesSequence)
    }

    @Test
    fun `Test 3 moves from bar on an empty point and then locked`() {
        player1Board.put(BAR_INDEX, 3)
        player2Board.put(toOpponentsIndex(21), 2)
        val movesSequence = listOf(movesSequence(move(BAR_INDEX, 23), move(BAR_INDEX, 23), move(BAR_INDEX, 23)))
        assertAllMovesFound(dice(2, 2), movesSequence)
    }

    @Test
    fun `Test moves from bar only`() {
        player1Board.put(BAR_INDEX, 4)
        val movesSequence = listOf(movesSequence(move(BAR_INDEX, 23), move(BAR_INDEX, 23), move(BAR_INDEX, 23), move(BAR_INDEX, 23)))
        assertAllMovesFound(dice(2, 2), movesSequence)
    }


    @Test
    fun `Test move from bar on an empty point and then further`() {
        player1Board.put(BAR_INDEX, 1)
        val movesSequence = listOf(movesSequence(move(BAR_INDEX, 23), move(23, 21), move(21, 19), move(19, 17)))
        assertAllMovesFound(dice(2, 2), movesSequence)
    }

    @Test
    fun `Test bar move and then standard move`() {
        player1Board.put(BAR_INDEX, 1)
        player1Board.put(24, 1)
        val movesSequence = listOf(
                movesSequence(move(BAR_INDEX, 23), move(23, 21), move(21, 19), move(19, 17)),
                movesSequence(move(BAR_INDEX, 23), move(23, 21), move(21, 19), move(24, 22)),
                movesSequence(move(BAR_INDEX, 23), move(23, 21), move(24, 22), move(22, 20)),
                movesSequence(move(BAR_INDEX, 23), move(24, 22), move(22, 20), move(20, 18)),
                movesSequence(move(BAR_INDEX, 23), move(24, 22), move(23, 21), move(21, 19)),
                movesSequence(move(BAR_INDEX, 23), move(24, 22), move(22, 20), move(23, 21)),
                movesSequence(move(BAR_INDEX, 23), move(24, 22), move(23, 21), move(22, 20))
        )
        assertAllMovesFound(dice(2, 2), movesSequence)
    }

    @Test
    fun `Test bar move, standard move with one sequential move and then locked`() {
        player1Board.put(BAR_INDEX, 1)
        player1Board.put(24, 1)
        player2Board.put(toOpponentsIndex(21), 2)
        player2Board.put(toOpponentsIndex(20), 2)
        val movesSequence = listOf(movesSequence(move(BAR_INDEX, 23), move(24, 22)))
        assertAllMovesFound(dice(2, 2), movesSequence)
    }

    @Test
    fun `Test bar move not possible`() {
        player1Board.put(BAR_INDEX, 1)
        player1Board.put(21, 5)
        player2Board.put(toOpponentsIndex(23), 2)
        assertNoMovesFound(dice(2, 2))
    }

//
//    @Test
//    fun `Test hit move from bar on single opponent's checker on point`() {
//        fail("Not implemented yet")
//    }
//
//
//    @Test
//    fun `Test untriedMoves not possible from bar on 2 of opponent's checkers on point`() {
//        fail("Not implemented yet")
//    }
//
//    @Test
//    fun `Test hit move on single opponent's checker on point and then locked`() {
//        fail("Not implemented yet")
//    }
//
//    @Test
//    fun `Test many untriedMoves possible`() {
//        fail("Not implemented yet")
//    }
//
//    @Ignore
//    @Test
//    fun testPerformance() {
//        fail("Not implemented yet")
//    }
//
//    @Test
//    fun `Test bear off move for new index just out of the board`() {
//        fail("Not implemented yet")
//    }
//
//    //
////
//    @Test
//    fun `Test bear off move for new index farther out of the board`() {
//        fail("Not implemented yet")
//    }
//
//    @Test
//    fun `Test bear off move not possible because not all checkers are in the home board`() {
//        fail("Not implemented yet")
//    }

}