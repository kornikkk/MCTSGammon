package pl.kkarolcz.mcts.mctsbackgammon.game.moves.search

import org.junit.Before
import pl.kkarolcz.mcts.mctsbackgammon.board.Board
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex
import pl.kkarolcz.mcts.mctsbackgammon.board.PlayerBoard
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.SingleMove
import pl.kkarolcz.mcts.mctsbackgammon.settings.TestSettings
import java.util.*

/**
 * Created by kkarolcz on 11.02.2018.
 */
open class AbstractFullMovesSearchTest {

    protected lateinit var player1Board: PlayerBoard
    protected lateinit var player2Board: PlayerBoard
    protected lateinit var dice: Dice

    protected lateinit var board: Board

    @Before
    fun initialize() {
        TestSettings.sortBoard = true

        player1Board = PlayerBoard()
        player2Board = PlayerBoard()
        player2Board.put(BoardIndex.BAR_INDEX, 1)
        board = Board(player1Board, player2Board)
    }

    protected fun dice(firstDice: Number, secondDice: Number) = Dice(firstDice.toByte(), secondDice.toByte())

    protected fun move(oldIndex: Number, newIndex: Number) = SingleMove(oldIndex.toByte(), newIndex.toByte())

    protected fun movesSequence(vararg moves: SingleMove) = FullMove(arrayOf(*moves), dice)

    protected fun movesSequence(moves: List<SingleMove>) = FullMove(moves.toTypedArray(), dice)

    protected fun toOpponentsIndex(index: Number) = BoardIndex.toOpponentsIndex(index.toByte())

    protected fun sortMoves(moves: Iterable<FullMove>): List<FullMove> {
        val movesList = moves.toMutableList()
        movesList.sortWith(Comparator { move1, move2 ->
            val moves1 = move1.toList()
            val moves2 = move2.toList()
            when {
                moves1.size > moves2.size -> return@Comparator -1
                moves1.size < moves2.size -> return@Comparator 1
                else ->
                    for (i in 0 until moves1.size) {
                        val singleMove1 = moves1[i]
                        val singleMove2 = moves2[i]
                        when {
                            singleMove1.oldIndex > singleMove2.oldIndex -> return@Comparator -1
                            singleMove1.oldIndex < singleMove2.oldIndex -> return@Comparator 1
                        }
                        when {
                            singleMove1.newIndex > singleMove2.newIndex -> return@Comparator -1
                            singleMove1.newIndex < singleMove2.newIndex -> return@Comparator 1
                        }
                    }
            }
            return@Comparator 0
        })

        return movesList
    }
}