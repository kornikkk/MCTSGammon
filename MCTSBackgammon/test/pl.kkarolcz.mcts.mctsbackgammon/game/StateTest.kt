package pl.kkarolcz.mcts.mctsbackgammon.game

import org.junit.Before
import pl.kkarolcz.mcts.mctsbackgammon.board.OLD_BackgammonBoard

/**
 * Created by kkarolcz on 28.08.2017.
 */
class StateTest {

    private val player1Checkers: ByteArray = ByteArray(OLD_BackgammonBoard.SIZE)
    private val player2Checkers: ByteArray = ByteArray(OLD_BackgammonBoard.SIZE)

    @Before
    fun initializeEmptyPlayerCheckersArrays() {
        player1Checkers.fill(0, 0, player1Checkers.size - 1)
        player2Checkers.fill(0, 0, player1Checkers.size - 1)
    }
//
//    @Test
//    fun `Test single checker possible moves`() {
//        player1Board[23] = 1
//        player2Board[1] = 1
//        val possibleMoves = getPossibleMoves(Dice(Die(1), Die(2)))
//
//        assertEquals(2, possibleMoves.size)
//
//        assertContainsMovesSequence(possibleMoves, singleMove(23, 22), singleMove(22, 20))
//        assertContainsMovesSequence(possibleMoves, singleMove(23, 21), singleMove(21, 20))
//    }
//
//    @Test
//    fun `Test single checker possible moves for doubling cube`() {
//        player1Board[23] = 1
//        player2Board[1] = 1
//        val possibleMoves = getPossibleMoves(Dice(Die(1), Die(1)))
//
//        assertEquals(1, possibleMoves.size)
//
//        assertContainsMovesSequence(possibleMoves, singleMove(23, 22), singleMove(22, 21), singleMove(21, 20), singleMove(20, 19))
//    }
//
//    @Test
//    fun `Test single checker moves not possible because of opponent's checkers`() {
//        player1Board[23] = 1
//        player2Board[3] = 2
//        player2Board[4] = 2
//        val possibleMoves = getPossibleMoves(Dice(Die(1), Die(2)))
//
//        assertEquals(0, possibleMoves.size)
//    }
//
//    @Test
//    fun `Test bear off moves`() {
//        player1Board[1] = 2
//        player2Board[1] = 1
//        val possibleMoves = getPossibleMoves(Dice(Die(2), Die(1)))
//
//        assertEquals(2, possibleMoves.size)
//        assertContainsMovesSequence(possibleMoves, singleBearingOffMove(1), singleBearingOffMove(1))
//        assertContainsMovesSequence(possibleMoves, singleBearingOffMove(1), singleBearingOffMove(1))
//    }
//
//    @Test
//    fun `Test bear off and normal moves`() {
//        player1Board[3] = 1
//        player1Board[2] = 1
//        player2Board[1] = 1
//        val possibleMoves = getPossibleMoves(Dice(Die(3), Die(2)))
//
//        assertEquals(3, possibleMoves.size)
//        assertContainsMovesSequence(possibleMoves, singleBearingOffMove(3), singleBearingOffMove(2))
//        assertContainsMovesSequence(possibleMoves, singleBearingOffMove(2), singleBearingOffMove(3))
//        assertContainsMovesSequence(possibleMoves, singleMove(3, 1), singleBearingOffMove(2))
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
//        val possibleMoves = getPossibleMoves(Dice(Die(4), Die(3)))
//        assertContainsMovesSequence(possibleMoves, singleMove(5, 2))
//        assertContainsMovesSequence(possibleMoves, singleMove(5, 1))
//        assertEquals(2, possibleMoves.size, "Wrong number of possible move sequences")
//    }
//
//    private fun assertContainsMovesSequence(possibleMoves: List<List<OLD_SingleBackgammonMove>>,
//                                            vararg singleMovesSequence: OLD_SingleBackgammonMove) {
//
//        assertTrue(possibleMoves.any { moves -> moves == asList(*singleMovesSequence) })
//    }
//
//    private fun asList(vararg singleMovesSequence: OLD_SingleBackgammonMove) = singleMovesSequence.toList()
//
//    private fun singleMove(oldIndex: Int, newIndex: Int) =
//            OLD_SingleBackgammonMove(OLD_BackgammonBoardIndex.of(oldIndex), OLD_BackgammonBoardIndex.of(newIndex))
//
//    private fun singleBearingOffMove(oldIndex: Int) =
//            OLD_SingleBackgammonMove(OLD_BackgammonBoardIndex.of(oldIndex), OLD_BackgammonBoardIndex.bearingOff())
//
//
//    private fun getPossibleMoves(dice: Dice) =
//            buildState(dice).findPossibleMoves().map { backgammonMovesSequence -> backgammonMovesSequence.moves }.toList()
//
//    private fun buildState(dice: Dice) = State(buildBoard(), Player.PLAYER_TWO, dice)
//
//    private fun buildBoard() = OLD_BackgammonBoard(
//            OLD_BackgammonPlayerCheckers(player1Board, 0),
//            OLD_BackgammonPlayerCheckers(player2Board, 0))


}