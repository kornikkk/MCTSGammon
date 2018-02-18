package pl.kkarolcz.mcts.mctsbackgammon.game.moves

/**
 * Created by kkarolcz on 11.02.2018.
 */
class FullMoveBuilder : Cloneable {

    private val moves = mutableListOf<BackgammonMove>()

    constructor(barMoves: List<BackgammonMove>) {
        this.moves.addAll(barMoves)
    }

    private constructor(other: FullMoveBuilder) {
        this.moves.addAll(other.moves)
    }

    public override fun clone(): FullMoveBuilder = FullMoveBuilder(this)

    fun append(move: BackgammonMove) {
        moves.add(move)
    }

    fun build(): BackgammonMovesSequence = BackgammonMovesSequence.create(moves)

}