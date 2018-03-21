package pl.kkarolcz.mcts.node.selectionpolicies

import pl.kkarolcz.mcts.MCTSMove
import pl.kkarolcz.mcts.MCTSNode
import pl.kkarolcz.mcts.MCTSState

/**
 * Created by kkarolcz on 19.08.2017.
 */
interface NodeSelectionPolicy {
    fun <N : MCTSNode<N, S, M>, S : MCTSState<M>, M : MCTSMove> selectChildNode(node: N): N
}