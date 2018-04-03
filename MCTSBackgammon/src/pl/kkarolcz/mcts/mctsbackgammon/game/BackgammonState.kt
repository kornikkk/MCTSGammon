package pl.kkarolcz.mcts.mctsbackgammon.game

import pl.kkarolcz.mcts.MCTSState
import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.Result
import pl.kkarolcz.mcts.mctsbackgammon.board.Board
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove
import pl.kkarolcz.utils.randomElement

/**
 * Created by kkarolcz on 24.08.2017.
 */
class BackgammonState : MCTSState<FullMove> {
    private val board: Board
    val dice: Dice?
    override val movesProvider: BackgammonMovesProvider

    override val result: Result?
        get() = when {
            !board.getPlayerBoard(Player.MCTS).anyLeftOnBoard -> Result(Result.PlayerResult.WIN, Result.PlayerResult.LOSE)
            !board.getPlayerBoard(Player.OPPONENT).anyLeftOnBoard -> Result(Result.PlayerResult.LOSE, Result.PlayerResult.WIN)
            else -> null
        }

    /**
     * Created manually
     */
    constructor(board: Board, currentPlayer: Player, dice: Dice?) : super(currentPlayer) {
        this.board = board
        this.dice = dice

        movesProvider = BackgammonIncrementalMovesProvider(board)
        movesProvider.resetDice(dice)
        movesProvider.findMovesForPlayer(currentPlayer)
    }

    /**
     * Expanding
     */
    private constructor(other: BackgammonState) : super(other.currentPlayer) {
        this.board = other.board.clone()
        this.dice = null

        movesProvider = BackgammonIncrementalMovesProvider(board)
        movesProvider.resetDice(dice)
    }

    private constructor(other: BackgammonState, dice: Dice) : super(other.currentPlayer) {
        this.board = other.board.clone()
        this.dice = dice

        movesProvider = BackgammonRandomMoveProvider(board)
        movesProvider.resetDice(dice)
        movesProvider.findMovesForPlayer(currentPlayer)
    }

    override fun copyForExpanding() = BackgammonState(this)

    override fun copyForPlayout(): BackgammonState = BackgammonState(this, Dice.PERMUTATIONS.randomElement())


    override fun doMoveImpl(move: FullMove) {
        for (currentMove in move)
            board.doSingleMove(currentPlayer, currentMove)
    }

    override fun beforeSwitchPlayerForPlayout() {
        movesProvider.resetDice(Dice.PERMUTATIONS.randomElement())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BackgammonState

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