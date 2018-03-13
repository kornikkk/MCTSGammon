package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import org.junit.Before
import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.board.Board
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex
import pl.kkarolcz.mcts.mctsbackgammon.board.PlayerBoard
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import java.util.*
import kotlin.test.assertEquals

/**
 * Created by kkarolcz on 11.02.2018.
 */
open class AbstractSingleMovesTest {

    protected lateinit var player1Board: PlayerBoard
    protected lateinit var player2Board: PlayerBoard
    private lateinit var board: Board

    @Before
    fun initializeEmptyPlayerCheckersArrays() {
        player1Board = PlayerBoard()
        player2Board = PlayerBoard()
        player2Board.put(BoardIndex.BAR_INDEX, 1)
        board = Board(player1Board, player2Board)
    }


    protected fun assertNoMovesFound(dice: Dice) {
        val searcher = FullMovesSearchNonDoubling(board, Player.MCTS, dice)
        assertEquals(emptyList(), searcher.findAll().toList())
    }

    protected fun assertAllMovesFound(dice: Dice, vararg expectedMoves: FullMove) {
        assertAllMovesFound(dice, expectedMoves.toList())
    }

    protected fun assertAllMovesFound(dice: Dice, expectedMoves: Iterable<FullMove>) {
        val expectedSet = sortMoves(expectedMoves)
        val actualSet = sortMoves(searcher(dice).findAll())
        assertEquals(expectedSet, actualSet)
    }

    protected fun searcher(dice: Dice) = when (dice.doubling) {
        true -> FullMovesSearchDoubling_V2(board, Player.MCTS, dice)
        false -> FullMovesSearchNonDoubling(board, Player.MCTS, dice)
    }

    protected fun dice(firstDice: Number, secondDice: Number) = Dice(firstDice.toByte(), secondDice.toByte())

    protected fun move(oldIndex: Number, newIndex: Number) = SingleMove(oldIndex.toByte(), newIndex.toByte())

    protected fun movesSequence(vararg moves: SingleMove) = FullMove(*moves)

    protected fun toOpponentsIndex(index: Number) = BoardIndex.toOpponentsIndex(index.toByte())

    private fun sortMoves(moves: Iterable<FullMove>): List<FullMove> {
        val movesList = moves.toMutableList()
        Collections.sort(movesList) { move1, move2 ->
            when {
                move1.moves.size > move2.moves.size -> return@sort -1
                move1.moves.size < move2.moves.size -> return@sort 1
                else ->
                    for (i in 0 until move1.moves.size) {
                        val singleMove1 = move1.moves[i]
                        val singleMove2 = move2.moves[i]
                        when {
                            singleMove1.oldIndex > singleMove2.oldIndex -> return@sort -1
                            singleMove1.oldIndex < singleMove2.oldIndex -> return@sort 1
                        }
                        when {
                            singleMove1.newIndex > singleMove2.newIndex -> return@sort -1
                            singleMove1.newIndex < singleMove2.newIndex -> return@sort 1
                        }
                    }
            }
            return@sort 0
        }

        return movesList
    }
}