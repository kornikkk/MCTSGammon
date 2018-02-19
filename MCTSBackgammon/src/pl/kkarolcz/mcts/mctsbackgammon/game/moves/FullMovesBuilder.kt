package pl.kkarolcz.mcts.mctsbackgammon.game.moves

/**
 * Created by kkarolcz on 11.02.2018.
 */
class FullMovesBuilder : Cloneable {

    private val moves = mutableListOf<SingleMove>()

    constructor(barMoves: List<SingleMove>) {
        this.moves.addAll(barMoves)
    }

    private constructor(other: FullMovesBuilder) {
        this.moves.addAll(other.moves)
    }

    public override fun clone(): FullMovesBuilder = FullMovesBuilder(this)

    fun append(move: SingleMove) {
        moves.add(move)
    }

    fun build(): FullMove = FullMove.create(moves)

}