package pl.kkarolcz.mctsgammon.mcts.node.selectionpolicies

import pl.kkarolcz.mctsgammon.mcts.MCTSNode
import pl.kkarolcz.mctsgammon.mcts.Move

/**
 * Created by kkarolcz on 19.08.2017.
 */
interface NodeSelectionPolicy {
    fun <M : Move> selectNode(childNodes: Iterable<MCTSNode<M>>): MCTSNode<M>
}