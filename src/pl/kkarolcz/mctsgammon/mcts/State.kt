package pl.kkarolcz.mctsgammon.mcts

import pl.kkarolcz.mctsgammon.utils.randomElement

/**
 * Created by kkarolcz on 07.08.2017.
 */
abstract class State<M : Move> : Cloneable {
    protected abstract val result: Result?
    abstract val previousPlayerId: Int
    val currentPlayerId: Int
        get() = 1 - previousPlayerId

    public abstract override fun clone(): State<M>

    protected abstract val moves: MutableList<M>

    fun hasMoves(): Boolean = !moves.isEmpty()

    abstract fun doMove(move: M)

    fun pollRandomMove(): M {
        val move = moves.randomElement()
        moves.remove(move)
        return move
    }

    fun playout(): Result? {
        val newState = clone()
        while (newState.moves.isNotEmpty()) {
            newState.doMove(newState.pollRandomMove())
        }
        return newState.result
    }

}