package pl.kkarolcz.mcts.mctsbackgammon

/**
 * Created by kkarolcz on 24.08.2017.
 */
class BackgammonState : Cloneable {
    val board: BackgammonBoard
    val currentPlayer: BackgammonPlayer
    val dice: BackgammonDices

    constructor(board: BackgammonBoard, currentPlayer: BackgammonPlayer, dices: BackgammonDices) {
        this.board = board
        this.currentPlayer = currentPlayer
        this.dice = dices
    }

    private constructor(other: BackgammonState) {
        this.board = other.board.clone()
        this.currentPlayer = other.currentPlayer
        this.dice = other.dice
    }

    public override fun clone(): BackgammonState {
        return BackgammonState(this)
    }

    fun possibleMoves() {
    }

}