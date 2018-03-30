package pl.kkarolcz.mcts.mctsbackgammon.game

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.board.Board
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.FullMovesSearch

/**
 * Created by kkarolcz on 24.02.2018.
 */
class BackgammonRandomMoveProvider(private val board: Board, private var player: Player) : BackgammonMovesProvider {
    private var dice: Dice? = null

    override fun hasUntriedMoves(): Boolean = dice != null

    override fun pollNextRandomUntriedMove(): FullMove {
        if (dice == null) throw IllegalStateException("Random move provider cannot be reused without resetting dice")
        return FullMovesSearch.findAll(board, player, dice!!).first()
    }

    override fun reset(player: Player) {
        this.player = player
        reset()
    }

    /**
     * Clears the moves and set a new dice
     * @param newDice new dice, cannot be null
     */
    override fun resetDice(newDice: Dice?) {
        if (newDice == null) throw IllegalArgumentException("Dice cannot be null in RandomMoveProvider")
        dice = newDice
    }

    private fun reset() {
        dice = null
    }

    override fun discardOtherDice(dice: Dice) {
        throw IllegalStateException("Random move provider cannot be used")
    }

}
