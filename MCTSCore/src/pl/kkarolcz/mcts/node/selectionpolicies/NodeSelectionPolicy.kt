package pl.kkarolcz.mcts.node.selectionpolicies

import pl.kkarolcz.mcts.MCTSMove
import pl.kkarolcz.mcts.MCTSNode

/**
 * Created by kkarolcz on 19.08.2017.
 */
interface NodeSelectionPolicy {
    fun <M : MCTSMove> selectNode(childNodes: Iterable<MCTSNode<M>>): MCTSNode<M>
}