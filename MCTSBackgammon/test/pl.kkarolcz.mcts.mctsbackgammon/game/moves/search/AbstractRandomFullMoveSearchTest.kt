package pl.kkarolcz.mcts.mctsbackgammon.game.moves.search

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMovesBuilder
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random.RandomFullMoveSearchDoubling
import kotlin.test.assertEquals
import kotlin.test.fail

/**
 * Created by kkarolcz on 11.02.2018.
 */
open class AbstractRandomFullMoveSearchTest : AbstractFullMovesSearchTest() {

    protected fun assertNoMoveFound() {
        assertEquals(FullMovesBuilder(dice).build(), findRandomMove(dice))
    }

    protected fun assertRandomMoveFound(expectedMoves: Iterable<FullMove>) {
        val expectedSet = sortMoves(expectedMoves)
        val randomMove = findRandomMove(dice)
        assertRandomMoveFoundAnyOrder(expectedSet, randomMove)
    }

    private fun assertRandomMoveFoundAnyOrder(expectedMoves: Iterable<FullMove>, randomMove: FullMove) {
        for (expectedMove in expectedMoves) {
            if (expectedMove.toSet() == randomMove.toSet())
                return
        }
        fail("Move not found")
    }

    private fun findRandomMove(dice: Dice): FullMove = when (dice.doubling) {
        true -> RandomFullMoveSearchDoubling(board, Player.MCTS, dice).findAll().first()
        false -> FullMovesSearchNonDoubling(board, Player.MCTS, dice).findAll().first()
    }

}