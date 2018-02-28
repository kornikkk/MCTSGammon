package pl.kkarolcz.mcts.node.selectionpolicies

import pl.kkarolcz.mcts.MCTSMove
import pl.kkarolcz.mcts.MCTSNode
import pl.kkarolcz.mcts.MCTSTraceableMove
import java.lang.Math.log
import java.lang.Math.sqrt

/**
 * Created by kkarolcz on 19.08.2017.
 */
class UCTNodeSelectionPolicy : NodeSelectionPolicy {

    companion object {
        val C = sqrt(2.0)
    }

    /**
     * @throws IllegalArgumentException if childNodes is empty
     */
    override fun <N : MCTSNode<N, M, T>, M : MCTSMove, T : MCTSTraceableMove.Trace> selectNode(childNodes: Iterable<N>): N {
        val totalVisits = childNodes.map(MCTSNode<N, M, T>::visits).sum()
        return childNodes.maxBy { node -> countUCT(totalVisits, node) } ?:
                throw IllegalArgumentException("Child nodes expected not empty")
    }

    private fun <N : MCTSNode<N, M, T>, M : MCTSMove, T : MCTSTraceableMove.Trace> countUCT(totalVisits: Int, node: N) =
            // wi / ni + c * √(ln(t) / ni)
            node.wins / node.visits + C * sqrt(log(totalVisits.toDouble()) / node.visits)

}