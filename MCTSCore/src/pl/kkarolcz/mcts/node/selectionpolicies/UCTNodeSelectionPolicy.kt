package pl.kkarolcz.mcts.node.selectionpolicies

import pl.kkarolcz.mcts.MCTSMove
import pl.kkarolcz.mcts.MCTSNode
import pl.kkarolcz.mcts.MCTSState
import java.lang.Math.log
import java.lang.Math.sqrt

/**
 * Created by kkarolcz on 19.08.2017.
 */
class UCTNodeSelectionPolicy : NodeSelectionPolicy {

    companion object {
        val C: Double = sqrt(2.0)
    }

    /**
     * @throws IllegalArgumentException if childNodes is empty
     */
    override fun <N : MCTSNode<N, S, M>, S : MCTSState<M>, M : MCTSMove> selectChildNode(node: N): N {
        return node.children.maxBy { child -> countUCT(node.visits, child) }
                ?: throw IllegalArgumentException("Child nodes expected not empty")
    }

    /**
     * Wi / Ni + C * √(ln(T) / Ni)
     */
    private fun <N : MCTSNode<N, S, M>, S : MCTSState<M>, M : MCTSMove> countUCT(totalVisits: Int, node: N): Double =
            node.wins.toDouble() / node.visits + C * sqrt(log(totalVisits.toDouble()) / node.visits)

}