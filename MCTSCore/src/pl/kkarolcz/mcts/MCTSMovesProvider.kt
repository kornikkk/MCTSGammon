package pl.kkarolcz.mcts

/**
 * Created by kkarolcz on 23.02.2018.
 */
interface MCTSMovesProvider<out M : MCTSMove, out T : MCTSTraceableMove.Trace> {
    fun hasUntriedMoves(): Boolean
    fun nextRandomUntriedMove(): MCTSTraceableMove<M, T>
    fun reset(player: Player)
}