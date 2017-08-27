package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon

import org.junit.Assert.assertEquals
import org.junit.Test
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonPlayer

/**
 * Created by kkarolcz on 23.07.2017.
 */
class PositionIDConverterTest {
    @Test
    fun `Test initial Position ID parsing`() {
        val board = PositionIDConverter.convert("4HPwATDgc/ABMA")

        // Player One
        val playerOneCheckersOnBoard = board.getPlayerCheckers(BackgammonPlayer.PLAYER_ONE)
        assertEquals(0, playerOneCheckersOnBoard[BackgammonBoardIndex.bar()])
        assertEquals(5, playerOneCheckersOnBoard[BackgammonBoardIndex.of(5)])
        assertEquals(3, playerOneCheckersOnBoard[BackgammonBoardIndex.of(7)])
        assertEquals(5, playerOneCheckersOnBoard[BackgammonBoardIndex.of(12)])
        assertEquals(2, playerOneCheckersOnBoard[BackgammonBoardIndex.of(23)])

        val playerTwoCheckersOnBoard = board.getPlayerCheckers(BackgammonPlayer.PLAYER_TWO)
        //Player Two
        assertEquals(0, playerTwoCheckersOnBoard[BackgammonBoardIndex.bar()])
        assertEquals(5, playerTwoCheckersOnBoard[BackgammonBoardIndex.of(5)])
        assertEquals(3, playerTwoCheckersOnBoard[BackgammonBoardIndex.of(7)])
        assertEquals(5, playerTwoCheckersOnBoard[BackgammonBoardIndex.of(12)])
        assertEquals(2, playerTwoCheckersOnBoard[BackgammonBoardIndex.of(23)])
    }

    @Test
    fun `Test random Position ID parsing`() {
        val board = PositionIDConverter.convert("kp/gATCwW/ABMA")

        // Player One
        val playerOneCheckersOnBoard = board.getPlayerCheckers(BackgammonPlayer.PLAYER_ONE)
        assertEquals(0, playerOneCheckersOnBoard[BackgammonBoardIndex.bar()])
        assertEquals(1, playerOneCheckersOnBoard[BackgammonBoardIndex.of(1)])
        assertEquals(1, playerOneCheckersOnBoard[BackgammonBoardIndex.of(3)])
        assertEquals(6, playerOneCheckersOnBoard[BackgammonBoardIndex.of(5)])
        assertEquals(1, playerOneCheckersOnBoard[BackgammonBoardIndex.of(7)])
        assertEquals(4, playerOneCheckersOnBoard[BackgammonBoardIndex.of(12)])
        assertEquals(2, playerOneCheckersOnBoard[BackgammonBoardIndex.of(23)])

        //Player Two
        val playerTwoCheckersOnBoard = board.getPlayerCheckers(BackgammonPlayer.PLAYER_TWO)
        assertEquals(0, playerTwoCheckersOnBoard[BackgammonBoardIndex.bar()])
        assertEquals(2, playerTwoCheckersOnBoard[BackgammonBoardIndex.of(4)])
        assertEquals(3, playerTwoCheckersOnBoard[BackgammonBoardIndex.of(5)])
        assertEquals(2, playerTwoCheckersOnBoard[BackgammonBoardIndex.of(6)])
        assertEquals(1, playerTwoCheckersOnBoard[BackgammonBoardIndex.of(7)])
        assertEquals(5, playerTwoCheckersOnBoard[BackgammonBoardIndex.of(12)])
        assertEquals(2, playerTwoCheckersOnBoard[BackgammonBoardIndex.of(23)])
    }
}