package pl.kkarolcz.mcts.mctsbackgammon.game.moves

/**
 * Created by kkarolcz on 11.02.2018.
 */
class FullMovesBuilder : Cloneable {

    private val moves: MutableList<SingleMove>

    val length get() = moves.size

    constructor() {
        this.moves = ArrayList(4)
    }

    constructor(barMoves: List<SingleMove>) {
        this.moves = ArrayList(barMoves)
    }

    private constructor(other: FullMovesBuilder) {
        this.moves = ArrayList(other.moves)
    }

    public override fun clone(): FullMovesBuilder = FullMovesBuilder(this)

    fun append(moves: List<SingleMove>): FullMovesBuilder {
        this.moves.addAll(moves)
        return this
    }

    fun append(vararg moves: SingleMove): FullMovesBuilder {
        this.moves.addAll(moves)
        return this
    }

    fun build(): FullMove = FullMove(moves)

}