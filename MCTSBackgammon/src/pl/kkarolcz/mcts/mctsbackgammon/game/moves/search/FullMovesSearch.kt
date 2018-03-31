package pl.kkarolcz.mcts.mctsbackgammon.game.moves.search

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.board.Board
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.allmoves.FullMovesSearchDoubling
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random.RandomFullMoveSearchDoubling
import pl.kkarolcz.utils.randomElement

/**
 * Created by kkarolcz on 23.02.2018.
 */
object FullMovesSearch {

    fun findAll(board: Board, player: Player, dice: Dice): MutableList<FullMove> = when (dice.doubling) {
        true -> FullMovesSearchDoubling(board, player, dice).findAll()
        false -> FullMovesSearchNonDoubling(board, player, dice).findAll()
    }

    fun findRandom(board: Board, player: Player, dice: Dice): FullMove = when (dice.doubling) {
        true -> RandomFullMoveSearchDoubling(board, player, dice).findAll().first()
        false -> FullMovesSearchNonDoubling(board, player, dice).findAll().randomElement()
    }
}