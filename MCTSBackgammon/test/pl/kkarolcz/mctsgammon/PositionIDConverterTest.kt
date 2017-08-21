package pl.kkarolcz.mctsgammon

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by kkarolcz on 23.07.2017.
 */
class PositionIDConverterTest {
    @Test
    fun `Test initial Position ID parsing`() {
        val board = PositionIDConverter.convert("4HPwATDgc/ABMA")
        // Player 0
        assertEquals(0, board[0][0])
        assertEquals(5, board[0][5])
        assertEquals(3, board[0][7])
        assertEquals(5, board[0][12])
        assertEquals(2, board[0][23])

        //Player 1
        assertEquals(0, board[1][0])
        assertEquals(5, board[1][5])
        assertEquals(3, board[1][7])
        assertEquals(5, board[1][12])
        assertEquals(2, board[1][23])
    }

    @Test
    fun `Test random Position ID parsing`() {
        val board = PositionIDConverter.convert("kp/gATCwW/ABMA")
        // Player 0
        assertEquals(0, board[0][0])
        assertEquals(1, board[0][1])
        assertEquals(1, board[0][3])
        assertEquals(6, board[0][5])
        assertEquals(1, board[0][7])
        assertEquals(4, board[0][12])
        assertEquals(2, board[0][23])

        //Player 1
        assertEquals(0, board[1][0])
        assertEquals(2, board[1][4])
        assertEquals(3, board[1][5])
        assertEquals(2, board[1][6])
        assertEquals(1, board[1][7])
        assertEquals(5, board[1][12])
        assertEquals(2, board[1][23])
    }
}