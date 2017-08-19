package pl.kkarolcz.mctsgammon.mcts.node.selectionpolicies

import pl.kkarolcz.mctsgammon.mcts.MCTSNode
import pl.kkarolcz.mctsgammon.mcts.Move
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
    override fun <M : Move> selectNode(childNodes: Iterable<MCTSNode<M>>): MCTSNode<M> {
        val totalVisits = childNodes.map(MCTSNode<M>::visits).sum()
        return childNodes.maxBy { node -> countUCT(totalVisits, node) } ?:
                throw IllegalArgumentException("Child nodes expected not empty")
    }

    private fun <M : Move> countUCT(totalVisits: Int, node: MCTSNode<M>) =
            // wi / ni + c * √(ln(t) / ni)
            node.wins / node.visits + C * sqrt(log(totalVisits.toDouble()) / node.visits)

}