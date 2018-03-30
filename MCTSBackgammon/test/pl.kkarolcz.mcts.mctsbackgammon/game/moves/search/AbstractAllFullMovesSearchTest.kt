package pl.kkarolcz.mcts.mctsbackgammon.game.moves.search

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove
import kotlin.test.assertEquals

/**
 * Created by kkarolcz on 11.02.2018.
 */
open class AbstractAllFullMovesSearchTest : AbstractFullMovesSearchTest() {

    protected fun assertNoMovesFound() {
        val searcher = FullMovesSearchNonDoubling(board, Player.MCTS, dice)
        assertEquals(listOf(FullMove(emptyArray(), dice)), searcher.findAll().toList())
    }

    protected fun assertAllMovesFound(vararg expectedMoves: FullMove) {
        assertAllMovesFound(expectedMoves.toList())
    }

    protected fun assertAllMovesFound(expectedMoves: Iterable<FullMove>) {
        val expectedSet = sortMoves(expectedMoves)
        val actualSet = sortMoves(findAll(dice))
        assertEquals(expectedSet, actualSet)
    }

    protected fun findAll(dice: Dice): List<FullMove> = when (dice.doubling) {
        true -> FullMovesSearchDoubling(board, Player.MCTS, dice).findAll()
        false -> FullMovesSearchNonDoubling(board, Player.MCTS, dice).findAll()
    }

}