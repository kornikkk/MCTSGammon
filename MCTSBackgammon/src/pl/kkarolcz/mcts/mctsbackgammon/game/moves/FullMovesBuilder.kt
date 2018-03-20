package pl.kkarolcz.mcts.mctsbackgammon.game.moves

/**
 * Created by kkarolcz on 11.02.2018.
 */
class FullMovesBuilder : Cloneable {

    private val moves = arrayOfNulls<SingleMove>(4)
    private var realSize = 0

    val length get() = realSize

    constructor()

    private constructor(other: FullMovesBuilder) {
        System.arraycopy(other.moves, 0, this.moves, 0, other.realSize)
        this.realSize = other.realSize
    }

    public override fun clone(): FullMovesBuilder = FullMovesBuilder(this)

    fun append(move: SingleMove): FullMovesBuilder {
        this.moves[realSize] = move
        this.realSize += 1
        return this
    }

    fun append(move1: SingleMove, move2: SingleMove): FullMovesBuilder {
        this.moves[realSize + 0] = move1
        this.moves[realSize + 1] = move2
        this.realSize += 2
        return this
    }

    fun append(move1: SingleMove, move2: SingleMove, move3: SingleMove): FullMovesBuilder {
        this.moves[realSize + 0] = move1
        this.moves[realSize + 1] = move2
        this.moves[realSize + 2] = move3
        this.realSize += 3
        return this
    }

    fun append(move1: SingleMove, move2: SingleMove, move3: SingleMove, move4: SingleMove): FullMovesBuilder {
        this.moves[realSize + 0] = move1
        this.moves[realSize + 1] = move2
        this.moves[realSize + 2] = move3
        this.moves[realSize + 3] = move4
        this.realSize += 4
        return this
    }

    @Suppress("UNCHECKED_CAST")
    fun build(): FullMove {
        val nonNullMoves: Array<SingleMove?> = arrayOfNulls(realSize)
        System.arraycopy(moves, 0, nonNullMoves, 0, realSize)
        return FullMove(nonNullMoves as Array<SingleMove>)
    }
}