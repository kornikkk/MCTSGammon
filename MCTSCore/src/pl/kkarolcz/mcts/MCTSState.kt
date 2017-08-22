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

    fun hasMoves(): Boolean = !moves.isEmpty()

    abstract fun doMove(move: M)

    internal fun pollRandomMove(): M {
        val move = moves.randomElement()
        moves.remove(move)
        return move
    }

    internal fun playout(): Result? {
        var newState = clone()
        while (newState.moves.isNotEmpty()) {
            newState.doMove(newState.pollRandomMove())
            newState.switchPlayer()
            newState = newState.clone()
        }
        return newState.result
    }

    fun switchPlayer() {
        previousPlayerId = currentPlayerId
    }
}