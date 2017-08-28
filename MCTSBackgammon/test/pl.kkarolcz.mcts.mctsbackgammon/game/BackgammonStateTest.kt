package pl.kkarolcz.mcts.mctsbackgammon.game

import org.junit.Before
import org.junit.Test
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoard
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonPlayerCheckers
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.BackgammonDices
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.SingleBackgammonMove
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Created by kkarolcz on 28.08.2017.
 */
class BackgammonStateTest {

    private val player1Checkers: IntArray = IntArray(BackgammonBoard.SIZE)
    private val player2Checkers: IntArray = IntArray(BackgammonBoard.SIZE)

    @Before
    fun initializeEmptyPlayerCheckersArrays() {
        player1Checkers.fill(0, 0, player1Checkers.size - 1)
        player2Checkers.fill(0, 0, player1Checkers.size - 1)
    }

    @Test
    fun `Test single checker possible moves`() {
        player1Checkers[23] = 1
        val possibleMoves = getPossibleMoves(BackgammonDices(Dice(1), Dice(2)))

        assertEquals(2, possibleMoves.size)

        assertContainsMovesSequence(possibleMoves, singleMove(23, 22), singleMove(22, 20))
        assertContainsMovesSequence(possibleMoves, singleMove(23, 21), singleMove(21, 20))
    }

    @Test
    fun `Test single checker possible moves for doubling cube`() {
        player1Checkers[23] = 1
        val possibleMoves = getPossibleMoves(BackgammonDices(Dice(1), Dice(1)))

        assertEquals(1, possibleMoves.size)

        assertContainsMovesSequence(possibleMoves, singleMove(23, 22), singleMove(22, 21), singleMove(21, 20), singleMove(20, 19))
    }

    @Test
    fun `Test single checker moves not possible because of opponent's checkers`() {
        player1Checkers[23] = 1
        player2Checkers[3] = 2
        player2Checkers[4] = 2
        val possibleMoves = getPossibleMoves(BackgammonDices(Dice(1), Dice(2)))

        assertEquals(0, possibleMoves.size)
    }

    @Test
    fun `Test bear off moves`() {
        player1Checkers[1] = 2
        val possibleMoves = getPossibleMoves(BackgammonDices(Dice(2), Dice(1)))

        assertEquals(2, possibleMoves.size)
        assertContainsMovesSequence(possibleMoves, singleBearingOffMove(1), singleBearingOffMove(1))
        assertContainsMovesSequence(possibleMoves, singleBearingOffMove(1), singleBearingOffMove(1))
    }

    @Test
    fun `Test bear off and normal moves`() {
        player1Checkers[3] = 1
        player1Checkers[2] = 1
        val possibleMoves = getPossibleMoves(BackgammonDices(Dice(3), Dice(2)))

        assertEquals(3, possibleMoves.size)
        assertContainsMovesSequence(possibleMoves, singleBearingOffMove(3), singleBearingOffMove(2))
        assertContainsMovesSequence(possibleMoves, singleBearingOffMove(2), singleBearingOffMove(3))
        assertContainsMovesSequence(possibleMoves, singleMove(3, 1), singleBearingOffMove(2))
    }

    private fun assertContainsMovesSequence(possibleMoves: List<List<SingleBackgammonMove>>,
                                            vararg singleMovesSequence: SingleBackgammonMove) {

        assertTrue(possibleMoves.any { singleMoves -> singleMoves.movesEqual(asList(*singleMovesSequence)) })
    }

    private fun asList(vararg singleMovesSequence: SingleBackgammonMove) = singleMovesSequence.toList()

    private fun singleMove(oldIndex: Int, newIndex: Int) =
            SingleBackgammonMove(BackgammonBoardIndex.of(oldIndex), BackgammonBoardIndex.of(newIndex))

    private fun singleBearingOffMove(oldIndex: Int) =
            SingleBackgammonMove(BackgammonBoardIndex.of(oldIndex), BackgammonBoardIndex.bearingOff())

    private fun List<SingleBackgammonMove>.movesEqual(other: List<SingleBackgammonMove>): Boolean {
        if (size != other.size)
            return false
        return (0 until size).all { this[it] == other[it] }
    }

    private fun getPossibleMoves(dices: BackgammonDices) =
            buildState(dices).possibleMoves().map { backgammonMovesSequence -> backgammonMovesSequence.moves.toList() }.toList()

    private fun buildState(dices: BackgammonDices) = BackgammonState(buildBoard(), BackgammonPlayer.PLAYER_ONE, dices)

    private fun buildBoard() = BackgammonBoard(BackgammonPlayerCheckers(player1Checkers), BackgammonPlayerCheckers(player2Checkers))


}