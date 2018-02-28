package pl.kkarolcz.mcts.mctsbackgammon.game

import pl.kkarolcz.mcts.MCTSState
import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.Result
import pl.kkarolcz.mcts.mctsbackgammon.board.Board
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.MovesProvider
import pl.kkarolcz.utils.randomElement

/**
 * Created by kkarolcz on 24.08.2017.
 */
class State(private val board: Board, override var currentPlayer: Player, val dice: Dice?) : MCTSState<FullMove, Dice>() {

    override val result: Result?
        get() = when {
            !board.getPlayerBoard(Player.MCTS).anyLeftOnBoard -> Result(Result.PlayerResult.WIN, Result.PlayerResult.LOSE)
            !board.getPlayerBoard(Player.OPPONENT).anyLeftOnBoard -> Result(Result.PlayerResult.LOSE, Result.PlayerResult.WIN)
            else -> null
        }

    override val movesProvider: MovesProvider = MovesProvider(board, currentPlayer)

    init {
        movesProvider.setDice(dice)
    }

    override fun copyForExpanding() = State(board.clone(), currentPlayer, null)

    override fun copyForPlayout() = State(board.clone(), currentPlayer, Dice.PERMUTATIONS.randomElement())

    override fun doMoveImpl(move: FullMove) {
        for (currentMove in move.moves)
            board.doSingleMove(currentPlayer, currentMove)
    }

    override fun afterSwitchPlayerForPlayout() {
        movesProvider.setDice(Dice.PERMUTATIONS.randomElement())
    }

    override fun toString(): String {
        val currentPlayer = "Current player: $currentPlayer"
        val result = this.result ?: return currentPlayer
        return currentPlayer + ", Winner: ${result.winner()}"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as State

        if (currentPlayer != other.currentPlayer) return false
        if (board != other.board) return false

        return true
    }

    override fun hashCode(): Int {
        var result = currentPlayer.hashCode()
        result = 31 * result + board.hashCode()
        return result
    }

}