package pl.kkarolcz.mctsgammon.mcts

import java.util.*

/**
 * Created by kkarolcz on 07.08.2017.
 */
abstract class State<M : Move> : Cloneable {
    private val random: Random = Random()

    protected abstract val result: Result
    protected val moves: MutableList<M> = mutableListOf()

    abstract val previousPlayerId: Int

    public abstract override fun clone(): State<M>

    fun hasMoves(): Boolean = moves.isEmpty()

    abstract fun doMove(move: Move)

    fun pollRandomMove(): M {
        val move = moves.randomElement()
        moves.remove(move)
        return move
    }

    fun rollout(): Result {
        val newState = clone()
        while (moves.isNotEmpty())
            newState.doMove(moves.randomElement())
        return newState.result
    }

}