package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import org.junit.Ignore
import org.junit.Test
import pl.kkarolcz.mcts.mctsbackgammon.board.Board
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.BAR_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.BEAR_OFF_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.PlayerBoard
import pl.kkarolcz.mcts.mctsbackgammon.game.Player
import java.util.*

/**
 * Created by kkarolcz on 27.08.2017.
 */
class BackgammonNonDoublingMovesTest : AbstractSingleMovesTest() {

    @Test
    fun `Test move from bar on an empty point and then locked`() {
        player1Board.put(BAR_INDEX, 1)
        player2Board.put(toOpponentsIndex(BAR_INDEX - 1), 2)
        player2Board.put(toOpponentsIndex(BAR_INDEX - 6 - 1), 2)

        val barMove = move(BAR_INDEX, 19)
        assertAllMovesFound(dice(6, 1), movesSequence(barMove))
    }

    @Test
    fun `Test move from bar on an empty point and then further`() {
        player1Board.put(BAR_INDEX, 1)

        val moveFromBar1 = move(BAR_INDEX, BAR_INDEX - 6)
        val nextMove1 = move(BAR_INDEX - 6, BAR_INDEX - 6 - 3)

        val moveFromBar2 = move(BAR_INDEX, BAR_INDEX - 3)
        val nextMove2 = move(BAR_INDEX - 3, BAR_INDEX - 3 - 6)
        assertAllMovesFound(dice(6, 3), movesSequence(moveFromBar1, nextMove1), movesSequence(moveFromBar2, nextMove2))
    }

    @Test
    fun `Test hit move from bar on single opponent's checker on point`() {
        player1Board.put(BAR_INDEX, 2)
        player2Board.put(toOpponentsIndex(BAR_INDEX - 6), 1)

        val firstBarMove = move(BAR_INDEX, BAR_INDEX - 6)
        val secondBarMove = move(BAR_INDEX, BAR_INDEX - 1)
        assertAllMovesFound(dice(6, 1), movesSequence(firstBarMove, secondBarMove))
    }


    @Test
    fun `Test moves not possible from bar on 2 of opponent's checkers on point`() {
        player1Board.put(BAR_INDEX, 2)
        player2Board.put(toOpponentsIndex(BAR_INDEX - 6), 2)
        player2Board.put(toOpponentsIndex(BAR_INDEX - 3), 2)

        assertNoMovesFound(dice(3, 6))
    }

    @Test
    fun `Test hit move on single opponent's checker on point and then locked`() {
        player1Board.put(24, 1)
        player2Board.put(toOpponentsIndex(24 - 2), 2) // Move would be possible for dice 2
        player2Board.put(toOpponentsIndex(24 - 6), 1) // That one should be hit
        player2Board.put(toOpponentsIndex(24 - 6 - 2), 2) // Move would be possible for dice 6, then 2

        val possibleMove = move(24, 24 - 6)
        assertAllMovesFound(dice(6, 2), movesSequence(possibleMove))
    }

    @Test
    fun `Test many moves possible`() {
        player1Board.put(24, 1)
        player1Board.put(22, 2)
        player1Board.put(10, 1)
        player2Board.put(toOpponentsIndex(10 - 4), 2)

        assertAllMovesFound(dice(5, 4),
                // Sequences from 24
                movesSequence(move(24, 24 - 5), move(24 - 5, 24 - 5 - 4)),
                movesSequence(move(24, 24 - 4), move(24 - 4, 24 - 4 - 5)),

                // Sequences from 22
                movesSequence(move(22, 22 - 5), move(22 - 5, 22 - 5 - 4)),
                movesSequence(move(22, 22 - 4), move(22 - 4, 22 - 4 - 5)),

                // Sequences from 10 (one possible point is blocked by opponent)
                movesSequence(move(10, 10 - 5), move(10 - 5, 10 - 5 - 4)),

                // Moves from the same tower
                movesSequence(move(22, 22 - 5), move(22, 22 - 4)),
                movesSequence(move(22, 22 - 4), move(22, 22 - 5)),

                // Combinations of single moves
                movesSequence(move(24, 24 - 5), move(22, 22 - 4)),
                movesSequence(move(24, 24 - 4), move(22, 22 - 5)),
                movesSequence(move(24, 24 - 4), move(10, 10 - 5)),

                movesSequence(move(22, 22 - 5), move(24, 24 - 4)),
                movesSequence(move(22, 22 - 4), move(24, 24 - 5)),
                movesSequence(move(22, 22 - 4), move(10, 10 - 5)),

                movesSequence(move(10, 10 - 5), move(24, 24 - 4)),
                movesSequence(move(10, 10 - 5), move(22, 22 - 4))
        )
    }

    @Ignore
    @Test
    fun testPerformance() {
        val storeMoves = true
        val allMoves = mutableListOf<FullMove>()
        val random = Random()
        for (i in 1..1_000_000) {
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
            val dices = dice(1 + random.nextInt(6), 1 + random.nextInt(6))
            val moves = FullMovesSearchNonDoubling(board, Player.PLAYER_ONE, dices).findAll()

            if (storeMoves)
                allMoves.addAll(moves)
        }
        return
    }

    //    @Test
//    fun `Test hit move not possible on 2 of opponent's checkers on point`() {
//        player1Board.put(24, 1)
//        player1Board.put(6, 2)
//
//        // Two opponents checkers so move is not possible with dice value = 5
//        val moves = possibleMoves(buildBoard(), Player.PLAYER_ONE, Die(5))
//        assertEquals(0, moves.count(), "Wrong number of possible moves. Should be empty")
//    }
//
    @Test
    fun `Test bear off move for new index just out of the board`() {
        player1Board.put(5, 1)

        val possibleMove = move(5, BEAR_OFF_INDEX)

        assertAllMovesFound(dice(6, 5), movesSequence(possibleMove))
    }

    //
//
    @Test
    fun `Test bear off move for new index farther out of the board`() {
        player1Board.put(2, 1)

        val possibleMove = move(2, BEAR_OFF_INDEX)
        assertAllMovesFound(dice(6, 5), movesSequence(possibleMove))
    }

    @Test
    fun `Test bear off move not possible because not all checkers are in the home board`() {
        player1Board.put(24, 1)
        player2Board.put(toOpponentsIndex(24 - 4), 2)
        player2Board.put(toOpponentsIndex(24 - 2), 2)

        player1Board.put(2, 1)

        assertNoMovesFound(dice(4, 2))
    }
//
//    @Test
//    fun `Test bear off not possible and normal moves not found`() {
//        player1Board.put(0, 1)
//        player1Board.put(2, 3)
//
//        player1Board.put(24, 1)
//        player1Board.put(20, 1)
//        player1Board.put(10, 1)
//
//        val moves = possibleMoves(buildBoard(), Player.PLAYER_ONE, Die(2))
//        assertEquals(0, moves.count(), "No moves should be possible")
//    }

//
//    @Test
//    fun `Test more moves`() {
//        player1Board[24] = 1
//        player2Board[3] = 1
//
//        player1Board[23] = 5
//        player2Board[4] = 2
//
//        player1Board[22] = 5
//        player2Board[5] = 0
//
//        player1Board[3] = 1
//        player2Board[24] = 0
//
//        val moves = possibleMoves(buildBoard(), Player.PLAYER_ONE, Die(2))
//        assertEquals(3, moves.count(), "Wrong number of possible moves")
//        assertNotNull(moves.find { move -> move.newCheckerIndex.toInt() == 22 })
//        assertNull(moves.find { move -> move.newCheckerIndex.toInt() == 21 })
//        assertNotNull(moves.find { move -> move.newCheckerIndex.toInt() == 20 })
//        assertNotNull(moves.find { move -> move.newCheckerIndex.toInt() == 1 })
//    }
//
//    @Test
//    fun `Test real case`() {
//        player2Board[3] = 5
//        player2Board[4] = 4
//        player2Board[5] = 3
//        player2Board[6] = 2
//        player2Board[7] = 1
//
//        player1Board[1] = 5
//        player1Board[2] = 1
//        player1Board[5] = 1
//        player1Board[23] = 2
//        player1Board[24] = 6
//
//        val movesFor3 = possibleMoves(buildBoard(), Player.PLAYER_ONE, Die(3))
//        assertNotNull(movesFor3.find { move -> move.newCheckerIndex.toInt() == 2 })
//
//        val movesFor4 = possibleMoves(buildBoard(), Player.PLAYER_ONE, Die(4))
//        assertNotNull(movesFor4.find { move -> move.newCheckerIndex.toInt() == 1 })
//    }


}