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

    private var hasNotPossibleMove = false

    public abstract override fun clone(): MCTSState<M>

    override fun toString() = "" +
            "Next Player ID: $currentPlayerId" +
            "\nWinner Player ID: ${result?.winner() ?: "NONE"}"

    fun hasUntriedMoves(): Boolean = hasNotPossibleMove || moves.isNotEmpty()

    fun doMove(move: M) {
        moves.remove(move)
        doMoveImpl(move)
    }

    abstract protected fun doMoveImpl(move: M)

    /**
     * Return random possible move if present or null for not possible move
     */
    internal fun pollRandomMove(): M? {
        if (moves.isNotEmpty()) {
            val move = moves.randomElement()
            moves.remove(move)
            return move
        }
        if (hasNotPossibleMove) {
            hasNotPossibleMove = false
            return null
        }

        throw IllegalStateException("Check if has not possible move or any untried moves are present before call")
    }

    internal fun playout(): Result? {
        val newState = clone()
        newState.updatePossibleMoves()

        while (newState.result == null && newState.hasUntriedMoves()) {
            val move = newState.pollRandomMove()
            if (move != null)
                newState.doMoveImpl(move)

            newState.switchPlayer()
        }
        return newState.result
    }

    internal fun switchPlayer() {
        beforeSwitchPlayer()
        previousPlayerId = currentPlayerId
        updatePossibleMoves()
    }

    internal fun updatePossibleMoves() {
        moves.clear()
        val possibleMoves = findPossibleMoves()
        hasNotPossibleMove = possibleMoves.isEmpty()
        if (!hasNotPossibleMove)
            moves.addAll(possibleMoves)
    }


    abstract fun beforeSwitchPlayer()

    abstract fun findPossibleMoves(): List<M>

    abstract override fun equals(other: Any?): Boolean

    abstract override fun hashCode(): Int
}