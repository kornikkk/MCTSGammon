package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import org.junit.Before
import org.junit.Test
import pl.kkarolcz.mcts.mctsbackgammon.board.*
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex.Companion.BAR_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonPlayer
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.BackgammonDices
import kotlin.test.assertEquals

/**
 * Created by kkarolcz on 27.08.2017.
 */
class BackgammonMoveTest {

    private lateinit var player1Checkers: BackgammonPlayerCheckers
    private lateinit var player2Checkers: BackgammonPlayerCheckers
    private lateinit var board: BackgammonBoard

    @Before
    fun initializeEmptyPlayerCheckersArrays() {
        player1Checkers = BackgammonPlayerCheckers()
        player2Checkers = BackgammonPlayerCheckers()
        player2Checkers.put(BAR_INDEX, 1)
        board = BackgammonBoard(player1Checkers, player2Checkers)
    }

    @Test
    fun `Test move from bar on an empty point and then locked`() {
        player1Checkers.put(BAR_INDEX, 1)
        player2Checkers.put(toOpponentsIndex(BAR_INDEX - 6 - 1), 2)

        val barMove = move(BAR_INDEX, 19)
        assertAllMovesFound(dices(6, 1), movesSequence(barMove))
    }

    @Test
    fun `Test move from bar on an empty point and then further`() {
        player1Checkers.put(BAR_INDEX, 1)

        val moveFromBar = move(BAR_INDEX, BAR_INDEX - 6)
        val nextMove = move(19, 19 - 3)
        assertAllMovesFound(dices(6, 3), movesSequence(moveFromBar, nextMove))
    }

    @Test
    fun `Test hit move from bar on single opponent's checker on point`() {
        player1Checkers.put(BAR_INDEX, 2)
        player2Checkers.put(toOpponentsIndex(BAR_INDEX - 6), 1)

        val firstBarMove = move(BAR_INDEX, BAR_INDEX - 6)
        val secondBarMove = move(BAR_INDEX, BAR_INDEX - 1)
        assertAllMovesFound(dices(6, 1), movesSequence(firstBarMove, secondBarMove))
    }


    @Test
    fun `Test moves not possible from bar on 2 of opponent's checkers on point`() {
        player1Checkers.put(BAR_INDEX, 2)
        player2Checkers.put(toOpponentsIndex(BAR_INDEX - 6), 2)
        player2Checkers.put(toOpponentsIndex(BAR_INDEX - 3), 2)

        assertNoMovesFound(dices(3, 6))
    }

    @Test
    fun `Test hit move on single opponent's checker on point and then locked`() {
        player1Checkers.put(24, 1)
        player2Checkers.put(toOpponentsIndex(24 - 2), 2) // Move would be possible for dice 2
        player2Checkers.put(toOpponentsIndex(24 - 6), 1) // That one should be hit
        player2Checkers.put(toOpponentsIndex(24 - 6 - 2), 2) // Move would be possible for dice 6, then 2

        val possibleMove = move(24, 24 - 6)
        assertAllMovesFound(dices(6, 2), movesSequence(possibleMove))
    }

    @Test
    fun `Test many moves possible`() {
        player1Checkers.put(24, 1)
        player1Checkers.put(22, 2)
        player1Checkers.put(10, 1)
        player2Checkers.put(toOpponentsIndex(10 - 4), 2)

        assertAllMovesFound(dices(5, 4),
                movesSequence(move(24, 24 - 5), move(24 - 5, 24 - 5 - 4)),
                movesSequence(move(24, 24 - 4), move(24 - 4, 24 - 4 - 5)),
                movesSequence(move(22, 22 - 5), move(22 - 5, 22 - 5 - 4)),
                movesSequence(move(22, 22 - 4), move(22 - 4, 22 - 4 - 5)),
                movesSequence(move(22, 22 - 5), move(22, 22 - 4)),
                movesSequence(move(10, 10 - 5), move(10 - 5, 10 - 5 - 4)),
                movesSequence(move(24, 24 - 5), move(22, 22 - 4)),
                movesSequence(move(22, 22 - 5), move(24, 24 - 4)),
                movesSequence(move(10, 10 - 5), move(24, 24 - 4)),
                movesSequence(move(10, 10 - 5), move(22, 22 - 4))
        )
    }

//    @Test
//    fun `Test hit move not possible on 2 of opponent's checkers on point`() {
//        player1Checkers.put(24, 1)
//        player1Checkers.put(6, 2)
//
//        // Two opponents checkers so move is not possible with dice value = 5
//        val moves = possibleMoves(buildBoard(), BackgammonPlayer.PLAYER_ONE, Dice(5))
//        assertEquals(0, moves.count(), "Wrong number of possible moves. Should be empty")
//    }
//
//    @Test
//    fun `Test bear off move for new index just out of the board`() {
//        player1Checkers.put(6, 1)
//
//        val moves = possibleMoves(buildBoard(), BackgammonPlayer.PLAYER_ONE, Dice(6))
//        assertEquals(-1, moves.first().newCheckerIndex.toInt())
//    }
//
//
//    @Test
//    fun `Test bear off move for new index farther out of the board`() {
//        player1Checkers.put(2, 1)
//
//        val moves = possibleMoves(buildBoard(), BackgammonPlayer.PLAYER_ONE, Dice(6))
//        assertEquals(-1, moves.first().newCheckerIndex.toInt())
//    }
//
//    @Test
//    fun `Test bear off move not possible because not checkers are in the home board`() {
//        player1Checkers.put(24, 1)
//        player1Checkers.put(2, 1)
//
//        val moves = possibleMoves(buildBoard(), BackgammonPlayer.PLAYER_ONE, Dice(6))
//        assertEquals(1, moves.count(), "Wrong number of possible moves")
//        assertNotNull(moves.find { move -> move.newCheckerIndex.toInt() == 18 })
//    }
//
//    @Test
//    fun `Test bear off not possible and normal moves not found`() {
//        player1Checkers.put(0, 1)
//        player1Checkers.put(2, 3)
//
//        player1Checkers.put(24, 1)
//        player1Checkers.put(20, 1)
//        player1Checkers.put(10, 1)
//
//        val moves = possibleMoves(buildBoard(), BackgammonPlayer.PLAYER_ONE, Dice(2))
//        assertEquals(0, moves.count(), "No moves should be possible")
//    }

//
//    @Test
//    fun `Test more moves`() {
//        player1Checkers[24] = 1
//        player2Checkers[3] = 1
//
//        player1Checkers[23] = 5
//        player2Checkers[4] = 2
//
//        player1Checkers[22] = 5
//        player2Checkers[5] = 0
//
//        player1Checkers[3] = 1
//        player2Checkers[24] = 0
//
//        val moves = possibleMoves(buildBoard(), BackgammonPlayer.PLAYER_ONE, Dice(2))
//        assertEquals(3, moves.count(), "Wrong number of possible moves")
//        assertNotNull(moves.find { move -> move.newCheckerIndex.toInt() == 22 })
//        assertNull(moves.find { move -> move.newCheckerIndex.toInt() == 21 })
//        assertNotNull(moves.find { move -> move.newCheckerIndex.toInt() == 20 })
//        assertNotNull(moves.find { move -> move.newCheckerIndex.toInt() == 1 })
//    }
//
//    @Test
//    fun `Test real case`() {
//        player2Checkers[3] = 5
//        player2Checkers[4] = 4
//        player2Checkers[5] = 3
//        player2Checkers[6] = 2
//        player2Checkers[7] = 1
//
//        player1Checkers[1] = 5
//        player1Checkers[2] = 1
//        player1Checkers[5] = 1
//        player1Checkers[23] = 2
//        player1Checkers[24] = 6
//
//        val movesFor3 = possibleMoves(buildBoard(), BackgammonPlayer.PLAYER_ONE, Dice(3))
//        assertNotNull(movesFor3.find { move -> move.newCheckerIndex.toInt() == 2 })
//
//        val movesFor4 = possibleMoves(buildBoard(), BackgammonPlayer.PLAYER_ONE, Dice(4))
//        assertNotNull(movesFor4.find { move -> move.newCheckerIndex.toInt() == 1 })
//    }

    fun assertNoMovesFound(dices: BackgammonDices) {
        val searcher = FullMovesSearchForNotDoubling(board, BackgammonPlayer.PLAYER_ONE, dices)
        assertEquals(searcher.findAll(), emptyList())
    }

    fun assertAllMovesFound(dices: BackgammonDices, vararg expectedMoves: BackgammonMovesSequence) {
        val searcher = FullMovesSearchForNotDoubling(board, BackgammonPlayer.PLAYER_ONE, dices)
        assertEquals(expectedMoves.toSet(), searcher.findAll().toSet())
    }

    fun dices(firstDice: Byte, secondDice: Byte) = BackgammonDices(firstDice, secondDice)

    fun move(oldIndex: Number, newIndex: Number) = BackgammonMove(oldIndex.toByte(), newIndex.toByte())

    fun movesSequence(vararg moves: BackgammonMove) = BackgammonMovesSequence(moves.toList())

    fun toOpponentsIndex(index: Number) = BackgammonBoardIndex.toOpponentsIndex(index.toByte())

    private fun buildBoard() = OLD_BackgammonBoard(
            OLD_BackgammonPlayerCheckers(byteArrayOf(), 0),
            OLD_BackgammonPlayerCheckers(byteArrayOf(), 0))

}