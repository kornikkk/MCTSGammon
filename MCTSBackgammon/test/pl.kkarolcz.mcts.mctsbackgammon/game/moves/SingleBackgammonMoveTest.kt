package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import org.junit.Before
import org.junit.Test
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoard
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonPlayerCheckers
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonPlayer
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Created by kkarolcz on 27.08.2017.
 */
class SingleBackgammonMoveTest {

    private val player1Checkers: IntArray = IntArray(BackgammonBoard.SIZE)
    private val player2Checkers: IntArray = IntArray(BackgammonBoard.SIZE)

    @Before
    fun initializeEmptyPlayerCheckersArrays() {
        player1Checkers.fill(0, 0, player1Checkers.size - 1)
        player2Checkers.fill(0, 0, player1Checkers.size - 1)
    }

    @Test
    fun `Test move from bar on an empty point`() {
        player1Checkers[0] = 2
        val movesFromBar = SingleBackgammonMove.possibleMoves(buildBoard(), BackgammonPlayer.PLAYER_ONE, Dice(6))

        assertEquals(19, movesFromBar.first().newCheckerIndex.toInt())
    }

    @Test
    fun `Test hit move from bar on single opponent's checker on point`() {
        player1Checkers[0] = 2
        player2Checkers[6] = 1
        val movesFromBar = SingleBackgammonMove.possibleMoves(buildBoard(), BackgammonPlayer.PLAYER_ONE, Dice(6))

        assertEquals(19, movesFromBar.first().newCheckerIndex.toInt())
    }


    @Test
    fun `Test move not possible from bar on 2 of opponent's checkers on point`() {
        player1Checkers[0] = 2
        player2Checkers[6] = 2
        val movesFromBar = SingleBackgammonMove.possibleMoves(buildBoard(), BackgammonPlayer.PLAYER_ONE, Dice(6))

        assertEquals(0, movesFromBar.count())
    }

    @Test
    fun `Test hit move on single opponent's checker on point`() {
        player1Checkers[24] = 1
        player2Checkers[6] = 1

        // One opponents checkers so move is not possible with dice value = 5
        val moves = SingleBackgammonMove.possibleMoves(buildBoard(), BackgammonPlayer.PLAYER_ONE, Dice(5))
        assertEquals(1, moves.count(), "Wrong number of possible moves")
        assertEquals(19, moves.first().newCheckerIndex.toInt())


    }

    @Test
    fun `Test hit move not possible on 2 of opponent's checkers on point`() {
        player1Checkers[24] = 1
        player2Checkers[6] = 2

        // Two opponents checkers so move is not possible with dice value = 5
        val moves = SingleBackgammonMove.possibleMoves(buildBoard(), BackgammonPlayer.PLAYER_ONE, Dice(5))
        assertEquals(0, moves.count(), "Wrong number of possible moves. Should be empty")
    }

    @Test
    fun `Test bear off move for new index just out of the board`() {
        player1Checkers[6] = 1

        val moves = SingleBackgammonMove.possibleMoves(buildBoard(), BackgammonPlayer.PLAYER_ONE, Dice(6))
        assertEquals(-1, moves.first().newCheckerIndex.toInt())
    }


    @Test
    fun `Test bear off move for new index farther out of the board`() {
        player1Checkers[2] = 1

        val moves = SingleBackgammonMove.possibleMoves(buildBoard(), BackgammonPlayer.PLAYER_ONE, Dice(6))
        assertEquals(-1, moves.first().newCheckerIndex.toInt())
    }

    @Test
    fun `Test bear off move not possible because not checkers are in the home board`() {
        player1Checkers[24] = 1
        player1Checkers[2] = 1

        val moves = SingleBackgammonMove.possibleMoves(buildBoard(), BackgammonPlayer.PLAYER_ONE, Dice(6))
        assertEquals(1, moves.count(), "Wrong number of possible moves")
        assertNotNull(moves.find { move -> move.newCheckerIndex.toInt() == 18 })
    }

    @Test
    fun `Test bear off not possible and normal moves not found`() {
        player1Checkers[0] = 1
        player2Checkers[2] = 3

        player1Checkers[24] = 1
        player1Checkers[20] = 1
        player1Checkers[10] = 1

        val moves = SingleBackgammonMove.possibleMoves(buildBoard(), BackgammonPlayer.PLAYER_ONE, Dice(2))
        assertEquals(0, moves.count(), "No moves should be possible")
    }


    @Test
    fun `Test more moves`() {
        player1Checkers[24] = 1
        player2Checkers[3] = 1

        player1Checkers[23] = 5
        player2Checkers[4] = 2

        player1Checkers[22] = 5
        player2Checkers[5] = 0

        player1Checkers[3] = 1
        player2Checkers[24] = 0

        val moves = SingleBackgammonMove.possibleMoves(buildBoard(), BackgammonPlayer.PLAYER_ONE, Dice(2))
        assertEquals(3, moves.count(), "Wrong number of possible moves")
        assertNotNull(moves.find { move -> move.newCheckerIndex.toInt() == 22 })
        assertNull(moves.find { move -> move.newCheckerIndex.toInt() == 21 })
        assertNotNull(moves.find { move -> move.newCheckerIndex.toInt() == 20 })
        assertNotNull(moves.find { move -> move.newCheckerIndex.toInt() == 1 })
    }

    private fun buildBoard() = BackgammonBoard(BackgammonPlayerCheckers(player1Checkers), BackgammonPlayerCheckers(player2Checkers))

}