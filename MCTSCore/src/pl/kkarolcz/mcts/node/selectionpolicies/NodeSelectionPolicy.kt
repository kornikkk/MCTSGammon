package pl.kkarolcz.mcts.node.selectionpolicies

import pl.kkarolcz.mcts.MCTSMove
import pl.kkarolcz.mcts.MCTSNode
import pl.kkarolcz.mcts.MCTSTraceableMove

/**
 * Created by kkarolcz on 19.08.2017.
 */
interface NodeSelectionPolicy {
    fun <N : MCTSNode<N, M, T>, M : MCTSMove, T : MCTSTraceableMove.Trace> selectNode(childNodes: Iterable<N>): N
}