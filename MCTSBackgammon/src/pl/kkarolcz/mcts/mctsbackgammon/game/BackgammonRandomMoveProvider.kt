package pl.kkarolcz.mcts.mctsbackgammon.game

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.board.Board
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.FullMovesSearch

/**
 * Created by kkarolcz on 24.02.2018.
 */
class BackgammonRandomMoveProvider(private val board: Board) : BackgammonMovesProvider {
    private var dice: Dice? = null
    private var randomMove: FullMove? = null

    override fun hasUntriedMoves(): Boolean = randomMove != null

    override fun pollNextRandomUntriedMove(): FullMove {
        return randomMove ?: throw IllegalStateException("Call resetDice method before polling random move")
    }

    override fun findMovesForPlayer(player: Player) {
        if (dice == null) throw IllegalStateException("Dice not set")

        randomMove = FullMovesSearch.findRandom(board, player, dice!!)
        dice = null
    }

    /**
     * Clears the moves and set a new dice
     * @param newDice new dice, cannot be null
     */
    override fun resetDice(newDice: Dice?) {
        if (newDice == null) throw IllegalArgumentException("Dice cannot be null in BackgammonRandomMoveProvider")

        dice = newDice
        randomMove = null

    }

    override fun discardOtherDice(dice: Dice) {
        throw IllegalStateException("Random move provider cannot be used here")
    }

}
