package pl.kkarolcz.mcts

import pl.kkarolcz.utils.randomElement

/**
 * Created by kkarolcz on 07.08.2017.
 */
abstract class MCTSState<M : MCTSMove> : Cloneable {
    val currentPlayerId: Int
        get() = 1 - previousPlayerId

    abstract val result: Result?

    abstract var previousPlayerId: Int

    protected val moves: MutableList<M> = mutableListOf()

    public abstract override fun clone(): MCTSState<M>

    override fun toString() = "" +
            "Next Player ID: $currentPlayerId" +
            "\nWinner Player ID: ${result?.winner() ?: "NONE"}"

    fun hasMoves(): Boolean = !moves.isEmpty()

    fun doMove(move: M) {
        moves.remove(move)
        doMoveImpl(move)
    }

    abstract fun doMoveImpl(move: M)

    internal fun pollRandomMove(): M {
        val move = moves.randomElement()
        moves.remove(move)
        return move
    }

    internal fun playout(): Result? {
        val newState = clone()
        newState.updatePossibleMoves()
        while (newState.hasMoves()) {
            val move = newState.pollRandomMove()
            newState.doMoveImpl(move) //TODO update possible moves and don't do that in cloning??? Or do that twice??
            newState.switchPlayer()
        }
        return newState.result
    }

    fun switchPlayer() {
        previousPlayerId = currentPlayerId
        updatePossibleMoves()
    }

    fun updatePossibleMoves() {
        moves.clear()
        moves.addAll(findPossibleMoves())
    }

    abstract fun findPossibleMoves(): MutableList<M>

    abstract override fun equals(other: Any?): Boolean

    abstract override fun hashCode(): Int
}