package pl.kkarolcz.mcts

import pl.kkarolcz.mctsgammon.utils.randomElement

/**
 * Created by kkarolcz on 07.08.2017.
 */
abstract class MCTSState<M : Move> : Cloneable {
    val currentPlayerId: Int
        get() = 1 - previousPlayerId

    abstract val result: Result?

    abstract var previousPlayerId: Int

    protected abstract val moves: MutableList<M>

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
        while (newState.hasMoves()) {
            val move = newState.pollRandomMove()
            newState.doMoveImpl(move)
            newState.switchPlayer()
        }
        return newState.result
    }

    fun switchPlayer() {
        previousPlayerId = currentPlayerId
    }
}