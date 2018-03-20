package pl.kkarolcz.mcts

/**
 * Created by kkarolcz on 07.08.2017.
 */
abstract class MCTSState<M : MCTSMove, T : MCTSTraceableMove.Trace> {
    abstract var currentPlayer: Player

    abstract val result: Result?

    protected abstract val movesProvider: MCTSMovesProvider<M, T>

    fun updateTrace(trace: T) {
        movesProvider.updateTrace(trace)
    }

    abstract fun copyForExpanding(): MCTSState<M, T>

    protected abstract fun copyForPlayout(): MCTSState<M, T>

    override fun toString() = "" +
            "Current Player: $currentPlayer" +
            "\nWinner: ${result?.winner() ?: "NONE"}"

    fun hasUntriedMoves(): Boolean = movesProvider.hasUntriedMoves()

    internal fun doMove(move: M) {
        // movesProvider.remove(move)
        doMoveImpl(move)
    }

    protected abstract fun doMoveImpl(move: M)

    protected abstract fun afterSwitchPlayerForPlayout()

    /**
     * Return random possible move if present or null for not possible move
     */
    internal fun pollRandomMove(): MCTSTraceableMove<M, T> = movesProvider.nextRandomUntriedMove()

    internal fun playout(): Result? {
        val newState = copyForPlayout()
        var newStateResult = newState.result

        while (newStateResult == null && newState.hasUntriedMoves()) {
            newState.doMoveImpl(newState.pollRandomMove().move)

            newState.switchPlayerForPlayout()
            newStateResult = newState.result
        }
        return newStateResult
    }

    internal fun switchPlayer() {
        currentPlayer = currentPlayer.opponent()
        movesProvider.reset(currentPlayer)
    }

    protected open fun switchPlayerForPlayout() {
        switchPlayer()
        afterSwitchPlayerForPlayout()
    }

    abstract override fun equals(other: Any?): Boolean

    abstract override fun hashCode(): Int

}