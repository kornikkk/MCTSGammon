package pl.kkarolcz.mcts

/**
 * Created by kkarolcz on 07.08.2017.
 */
abstract class MCTSState<M : MCTSMove>(currentPlayer: Player) {
    var currentPlayer = currentPlayer
        private set

    abstract val result: Result?

    abstract val movesProvider: MCTSMovesProvider<M>


    abstract override fun equals(other: Any?): Boolean

    abstract override fun hashCode(): Int

    override fun toString(): String {
        val result = this.result
        return when (result) {
            null -> "Current player: $currentPlayer"
            else -> "Current player: $currentPlayer, Winner: ${result.winner()}"
        }
    }


    abstract fun copyForExpanding(): MCTSState<M>

    protected abstract fun copyForPlayout(): MCTSState<M>

    protected abstract fun doMoveImpl(move: M)

    protected abstract fun beforeSwitchPlayerForPlayout()


    internal fun hasUntriedMoves(): Boolean = movesProvider.hasUntriedMoves()

    internal fun pollRandomUntriedMove(): M = movesProvider.pollNextRandomUntriedMove()

    internal fun doMove(move: M) {
        doMoveImpl(move)
    }

    internal fun playout(): Result {
        val newState = copyForPlayout()
        var newStateResult = newState.result

        while (newStateResult == null) {
            newState.doMove(newState.pollRandomUntriedMove())
            newState.beforeSwitchPlayerForPlayout()
            newState.switchPlayer()

            newStateResult = newState.result
        }

        return newStateResult
    }

    internal fun switchPlayer() {
        currentPlayer = currentPlayer.opponent()
        movesProvider.findMovesForPlayer(currentPlayer)
    }

}