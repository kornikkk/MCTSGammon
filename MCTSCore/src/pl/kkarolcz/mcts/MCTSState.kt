package pl.kkarolcz.mcts

/**
 * Created by kkarolcz on 07.08.2017.
 */
abstract class MCTSState<M : MCTSMove>(private var _currentPlayer: Player) {

    val currentPlayer get() = _currentPlayer


    abstract val result: Result?

    abstract val movesProvider: MCTSMovesProvider<M>


    abstract override fun equals(other: Any?): Boolean

    abstract override fun hashCode(): Int

 
    abstract fun copyForExpanding(): MCTSState<M>

    protected abstract fun copyForPlayout(): MCTSState<M>

    protected abstract fun doMoveImpl(move: M)

    protected abstract fun afterSwitchPlayerForPlayout()


    internal fun hasUntriedMoves(): Boolean = movesProvider.hasUntriedMoves()

    internal fun doMove(move: M) {
        doMoveImpl(move)
    }

    /**
     * Return random possible move if present or null for not possible move
     */
    internal fun pollRandomMove(): M = movesProvider.pollNextRandomUntriedMove()

    internal fun playout(): Result? {
        val newState = copyForPlayout()
        var newStateResult = newState.result

        while (newStateResult == null && newState.hasUntriedMoves()) {
            newState.doMove(newState.pollRandomMove())
            newState.switchPlayerForPlayout()

            newStateResult = newState.result
        }

        return newStateResult
    }

    internal fun switchPlayer() {
        _currentPlayer = _currentPlayer.opponent()
        movesProvider.reset(_currentPlayer)
    }

    private fun switchPlayerForPlayout() {
        switchPlayer()
        afterSwitchPlayerForPlayout()
    }


    override fun toString(): String {
        val result = this.result
        return when (result) {
            null -> "Current player: $_currentPlayer"
            else -> "Current player: $_currentPlayer, Winner: ${result.winner()}"
        }
    }

}