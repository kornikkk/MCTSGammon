package pl.kkarolcz.mcts.gnubackgammon

import org.junit.Assert.assertEquals
import org.junit.Test
import pl.kkarolcz.mcts.mctsbackgammon.BackgammonPlayer

/**
 * Created by kkarolcz on 23.07.2017.
 */
class PositionIDConverterTest {
    @Test
    fun `Test initial Position ID parsing`() {
        val board = PositionIDConverter.convert("4HPwATDgc/ABMA")

        // Player One
        val playerOneCheckersOnBoard = board.getPlayerCheckers(BackgammonPlayer.PLAYER_ONE)
        assertEquals(0, playerOneCheckersOnBoard[0])
        assertEquals(5, playerOneCheckersOnBoard[5])
        assertEquals(3, playerOneCheckersOnBoard[7])
        assertEquals(5, playerOneCheckersOnBoard[12])
        assertEquals(2, playerOneCheckersOnBoard[23])

        val playerTwoCheckersOnBoard = board.getPlayerCheckers(BackgammonPlayer.PLAYER_TWO)
        //Player Two
        assertEquals(0, playerTwoCheckersOnBoard[0])
        assertEquals(5, playerTwoCheckersOnBoard[5])
        assertEquals(3, playerTwoCheckersOnBoard[7])
        assertEquals(5, playerTwoCheckersOnBoard[12])
        assertEquals(2, playerTwoCheckersOnBoard[23])
    }

    @Test
    fun `Test random Position ID parsing`() {
        val board = PositionIDConverter.convert("kp/gATCwW/ABMA")

        // Player One
        val playerOneCheckersOnBoard = board.getPlayerCheckers(BackgammonPlayer.PLAYER_ONE)
        assertEquals(0, playerOneCheckersOnBoard[0])
        assertEquals(1, playerOneCheckersOnBoard[1])
        assertEquals(1, playerOneCheckersOnBoard[3])
        assertEquals(6, playerOneCheckersOnBoard[5])
        assertEquals(1, playerOneCheckersOnBoard[7])
        assertEquals(4, playerOneCheckersOnBoard[12])
        assertEquals(2, playerOneCheckersOnBoard[23])

        //Player Two
        val playerTwoCheckersOnBoard = board.getPlayerCheckers(BackgammonPlayer.PLAYER_TWO)
        assertEquals(0, playerTwoCheckersOnBoard[0])
        assertEquals(2, playerTwoCheckersOnBoard[4])
        assertEquals(3, playerTwoCheckersOnBoard[5])
        assertEquals(2, playerTwoCheckersOnBoard[6])
        assertEquals(1, playerTwoCheckersOnBoard[7])
        assertEquals(5, playerTwoCheckersOnBoard[12])
        assertEquals(2, playerTwoCheckersOnBoard[23])
    }
}