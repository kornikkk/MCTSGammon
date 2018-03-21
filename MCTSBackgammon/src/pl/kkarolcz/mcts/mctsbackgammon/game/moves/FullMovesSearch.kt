package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.board.Board
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice

/**
 * Created by kkarolcz on 23.02.2018.
 */
object FullMovesSearch {

    fun findAll(board: Board, player: Player, dice: Dice): MutableList<FullMove> = when (dice.doubling) {
        true -> FullMovesSearchDoubling(board, player, dice).findAll()
        false -> FullMovesSearchNonDoubling(board, player, dice).findAll()
    }

}