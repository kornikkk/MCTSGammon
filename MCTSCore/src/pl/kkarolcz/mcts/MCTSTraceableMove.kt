package pl.kkarolcz.mcts

/**
 * Created by kkarolcz on 25.02.2018.
 */
class MCTSTraceableMove<out M : MCTSMove, out T : MCTSTraceableMove.Trace>(val move: M, val trace: T) {

    interface Trace {
        override fun equals(other: Any?): Boolean
        override fun hashCode(): Int
    }

}
