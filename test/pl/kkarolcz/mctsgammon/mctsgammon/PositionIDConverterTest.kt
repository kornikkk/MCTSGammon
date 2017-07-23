package pl.kkarolcz.mctsgammon.mctsgammon

import org.junit.Assert.assertEquals
import org.junit.Test
import pl.kkarolcz.mctsgammon.gnubackgammon.PositionIDConverter

/**
 * Created by kkarolcz on 23.07.2017.
 */
class PositionIDConverterTest {
    @Test
    fun testInitialPositionId() {
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
}