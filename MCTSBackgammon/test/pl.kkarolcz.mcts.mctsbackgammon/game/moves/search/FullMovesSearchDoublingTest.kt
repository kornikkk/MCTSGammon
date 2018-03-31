package pl.kkarolcz.mcts.mctsbackgammon.game.moves.search

import org.junit.Ignore
import org.junit.Test
import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.board.Board
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.BAR_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.BEAR_OFF_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.PlayerBoard
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.allmoves.FullMovesSearchDoubling
import pl.kkarolcz.mcts.mctsbackgammon.settings.TestSettings
import java.util.*

/**
 * Created by kkarolcz on 27.08.2017.
 */
class FullMovesSearchDoublingTest : AbstractAllFullMovesSearchTest() {


    @Ignore
    @Test
    fun testPerformance2() {
        TestSettings.sortBoard = false
        val player1Checkers = PlayerBoard()
        for (i in 1..15)
            player1Checkers.put((BAR_INDEX - i.toByte()).toByte(), 1)

        val board = Board(player1Checkers, PlayerBoard())
        val dices = dice(3, 3)

        val attempts = 20000

        for (i in 1..15000) { // Warm up
            FullMovesSearchDoubling(board, Player.MCTS, dices).findAll()
        }

        var fullTime = 0L
        for (i in 1..attempts) {
            val startTime = System.nanoTime()
            FullMovesSearchDoubling(board, Player.MCTS, dices).findAll()
            val endTime = System.nanoTime()
            fullTime += endTime - startTime
        }

        println("Average time: ${fullTime / 1000 / attempts} μs")
    }

    @Ignore
    @Test
    fun testPerformance() {
        TestSettings.sortBoard = false
        val random = Random()

        var fullTime = 0L
        val attempts = 100_000
        for (i in 1..attempts) {
            val player1Checkers = PlayerBoard()
            val player1RandomCheckers = ByteArray(26)

            val player2Checkers = PlayerBoard()
            val player2RandomCheckers = ByteArray(26)

            for (j in 1..random.nextInt(15) + 1) {
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

            val startTime = System.nanoTime()
            FullMovesSearchDoubling(board, Player.MCTS, dices).findAll()
            val endTime = System.nanoTime()
            fullTime += endTime - startTime
        }
        println("Average time: ${fullTime / 1000 / attempts} μs")
    }

    @Test
    fun `Test single checker move`() {
        player1Board.put(24, 1)
        dice = dice(2, 2)

        val movesSequence = movesSequence(
                move(24, 22),
                move(22, 20),
                move(20, 18),
                move(18, 16)
        )
        assertAllMovesFound(movesSequence)
    }

    @Test
    fun `Test multiple checkers move`() {
        player1Board.put(24, 1)
        player1Board.put(23, 1)
        dice = dice(2, 2)

        val movesSequence = listOf(
                movesSequence(move(24, 22), move(22, 20), move(20, 18), move(18, 16)),
                movesSequence(move(24, 22), move(23, 21), move(22, 20), move(20, 18)),
                movesSequence(move(24, 22), move(23, 21), move(21, 19), move(19, 17)),
                movesSequence(move(24, 22), move(23, 21), move(22, 20), move(21, 19)),
                movesSequence(move(23, 21), move(21, 19), move(19, 17), move(17, 15))
        )
        assertAllMovesFound(movesSequence)
    }

    @Test
    fun `Test one locked checker move`() {
        player1Board.put(24, 2)
        player2Board.put(toOpponentsIndex(18), 2)
        dice = dice(2, 2)

        val movesSequence = listOf(
                movesSequence(move(24, 22), move(24, 22), move(22, 20), move(22, 20))
        )
        assertAllMovesFound(movesSequence)
    }

    @Test
    fun `Test move from bar on an empty point and then locked`() {
        player1Board.put(BAR_INDEX, 1)
        player2Board.put(toOpponentsIndex(21), 2)
        dice = dice(2, 2)

        val movesSequence = listOf(movesSequence(move(BAR_INDEX, 23)))
        assertAllMovesFound(movesSequence)
    }

    @Test
    fun `Test 2 moves from bar on an empty point and then locked`() {
        player1Board.put(BAR_INDEX, 2)
        player2Board.put(toOpponentsIndex(21), 2)
        dice = dice(2, 2)

        val movesSequence = listOf(movesSequence(move(BAR_INDEX, 23), move(BAR_INDEX, 23)))
        assertAllMovesFound(movesSequence)
    }

    @Test
    fun `Test 3 moves from bar on an empty point and then locked`() {
        player1Board.put(BAR_INDEX, 3)
        player2Board.put(toOpponentsIndex(21), 2)
        dice = dice(2, 2)

        val movesSequence = listOf(movesSequence(move(BAR_INDEX, 23), move(BAR_INDEX, 23), move(BAR_INDEX, 23)))
        assertAllMovesFound(movesSequence)
    }

    @Test
    fun `Test moves from bar only`() {
        player1Board.put(BAR_INDEX, 4)
        dice = dice(2, 2)

        val movesSequence = listOf(movesSequence(move(BAR_INDEX, 23), move(BAR_INDEX, 23), move(BAR_INDEX, 23), move(BAR_INDEX, 23)))
        assertAllMovesFound(movesSequence)
    }


    @Test
    fun `Test move from bar on an empty point and then further`() {
        player1Board.put(BAR_INDEX, 1)
        dice = dice(2, 2)

        val movesSequence = listOf(movesSequence(move(BAR_INDEX, 23), move(23, 21), move(21, 19), move(19, 17)))
        assertAllMovesFound(movesSequence)
    }

    @Test
    fun `Test bar move and then standard move`() {
        player1Board.put(BAR_INDEX, 1)
        player1Board.put(24, 1)
        dice = dice(2, 2)

        val movesSequence = listOf(
                movesSequence(move(BAR_INDEX, 23), move(23, 21), move(21, 19), move(19, 17)),
                movesSequence(move(BAR_INDEX, 23), move(24, 22), move(23, 21), move(21, 19)),
                movesSequence(move(BAR_INDEX, 23), move(24, 22), move(23, 21), move(22, 20)),
                movesSequence(move(BAR_INDEX, 23), move(24, 22), move(22, 20), move(20, 18))
        )
        assertAllMovesFound(movesSequence)
    }

    @Test
    fun `Test bar move, standard move with one sequential move and then locked`() {
        player1Board.put(BAR_INDEX, 1)
        player1Board.put(24, 1)
        player2Board.put(toOpponentsIndex(21), 2)
        player2Board.put(toOpponentsIndex(20), 2)
        dice = dice(2, 2)

        val movesSequence = listOf(movesSequence(move(BAR_INDEX, 23), move(24, 22)))
        assertAllMovesFound(movesSequence)
    }

    @Test
    fun `Test bar move not possible`() {
        player1Board.put(BAR_INDEX, 1)
        player1Board.put(21, 5)
        player2Board.put(toOpponentsIndex(23), 2)
        dice = dice(2, 2)

        assertNoMovesFound()
    }

    @Test
    fun `Test sequential moves 1`() {
        player1Board.put(24, 1)
        player1Board.put(23, 1)
        player2Board.put(toOpponentsIndex(19), 2)
        dice = dice(2, 2)


        val movesSequence = listOf(
                movesSequence(move(24, 22), move(22, 20), move(20, 18), move(18, 16)),
                movesSequence(move(24, 22), move(23, 21), move(22, 20), move(20, 18))
        )
        assertAllMovesFound(movesSequence)
    }

    @Test
    fun `Test only partial moves full move possible`() {
        player1Board.put(20, 1)
        player2Board.put(toOpponentsIndex(18), 2)

        player1Board.put(15, 1)
        player2Board.put(toOpponentsIndex(13), 2)

        player1Board.put(10, 1)
        player2Board.put(toOpponentsIndex(8), 2)

        player1Board.put(5, 1)
        player2Board.put(toOpponentsIndex(3), 2)

        dice = dice(1, 1)


        val movesSequence = listOf(
                movesSequence(move(20, 19), move(15, 14), move(10, 9), move(5, 4))
        )
        assertAllMovesFound(movesSequence)
    }


    @Test
    fun `Test bar move and 3 partial moves`() {
        player1Board.put(BAR_INDEX, 1)
        player1Board.put(24, 1)
        player1Board.put(16, 1)
        player1Board.put(10, 1)

        player2Board.put(toOpponentsIndex(20), 2)
        player2Board.put(toOpponentsIndex(12), 2)

        dice = dice(2, 2)

        val movesSequence = listOf(
                movesSequence(move(BAR_INDEX, 23), move(24, 22), move(23, 21), move(21, 19)),
                movesSequence(move(BAR_INDEX, 23), move(24, 22), move(16, 14), move(10, 8)),
                movesSequence(move(BAR_INDEX, 23), move(23, 21), move(21, 19), move(19, 17)),
                movesSequence(move(BAR_INDEX, 23), move(16, 14), move(23, 21), move(21, 19)),
                movesSequence(move(BAR_INDEX, 23), move(10, 8), move(23, 21), move(21, 19)),
                movesSequence(move(BAR_INDEX, 23), move(10, 8), move(23, 21), move(8, 6)),
                movesSequence(move(BAR_INDEX, 23), move(10, 8), move(8, 6), move(6, 4))
        )
        assertAllMovesFound(movesSequence)
    }

    @Test
    fun `Test simple bearing off`() {
        player1Board.put(6, 4)
        dice = dice(6, 6)
        val movesSequence = listOf(
                movesSequence(move(6, BEAR_OFF_INDEX), move(6, BEAR_OFF_INDEX), move(6, BEAR_OFF_INDEX), move(6, BEAR_OFF_INDEX))
        )

        assertAllMovesFound(movesSequence)
    }

    @Test
    fun `Test bearing off with one checker outside the home board`() {
        player1Board.put(10, 1)
        player1Board.put(6, 3)
        dice = dice(6, 6)
        val movesSequence = listOf(
                movesSequence(move(10, 4), move(6, BEAR_OFF_INDEX), move(6, BEAR_OFF_INDEX), move(6, BEAR_OFF_INDEX))
        )

        assertAllMovesFound(movesSequence)
    }

    @Test
    fun `Test bearing off with one checker outside the home board which needs the sequence to get into the home board`() {
        player1Board.put(14, 1)
        player1Board.put(6, 3)
        dice = dice(6, 6)
        val movesSequence = listOf(
                movesSequence(move(14, 8), move(8, 2), move(6, BEAR_OFF_INDEX), move(6, BEAR_OFF_INDEX))
        )

        assertAllMovesFound(movesSequence)
    }

    @Test
    fun `Test bearing off with one checker outside the home board which needs two sequences to get into the home board`() {
        player1Board.put(22, 1)
        player1Board.put(6, 3)
        dice = dice(6, 6)
        val movesSequence = listOf(
                movesSequence(move(22, 16), move(16, 10), move(10, 4), move(6, BEAR_OFF_INDEX))
        )

        assertAllMovesFound(movesSequence)
    }

    @Test
    fun `Test bearing off with two checkers outside the home board`() {
        player1Board.put(10, 2)
        player1Board.put(6, 4)
        dice = dice(6, 6)
        val movesSequence = listOf(
                movesSequence(move(10, 4), move(10, 4), move(6, BEAR_OFF_INDEX), move(6, BEAR_OFF_INDEX))
        )

        assertAllMovesFound(movesSequence)
    }

    @Test
    fun `Test bearing off with three checkers outside the home board`() {
        player1Board.put(11, 1)
        player1Board.put(10, 2)
        player1Board.put(6, 4)
        dice = dice(6, 6)
        val movesSequence = listOf(
                movesSequence(move(11, 5), move(10, 4), move(10, 4), move(6, BEAR_OFF_INDEX))
        )

        assertAllMovesFound(movesSequence)
    }

    @Test
    fun `Test bearing off with partial moves inside the home board`() {
        player1Board.put(6, 2)
        dice = dice(4, 4)
        val movesSequence = listOf(
                movesSequence(move(6, 2), move(6, 2), move(2, BEAR_OFF_INDEX), move(2, BEAR_OFF_INDEX))
        )

        assertAllMovesFound(movesSequence)
    }

    @Test
    fun `Test bearing off with partial and sequential move inside the home board`() {
        player1Board.put(6, 2)
        dice = dice(2, 2)
        val movesSequence = listOf(
                movesSequence(move(6, 4), move(6, 4), move(4, 2), move(4, 2)),
                movesSequence(move(6, 4), move(6, 4), move(4, 2), move(2, BEAR_OFF_INDEX)),
                movesSequence(move(6, 4), move(6, 4), move(4, 2), move(2, BEAR_OFF_INDEX))
        )

        assertAllMovesFound(movesSequence)
    }

    //TODO Check some more bar moves (like 3 moves and then sequence or 3 moves and then partial move)

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