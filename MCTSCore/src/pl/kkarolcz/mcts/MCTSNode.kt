package pl.kkarolcz.mcts

import pl.kkarolcz.mcts.node.selectionpolicies.NodeSelectionPolicy

/**
 * Created by kkarolcz on 07.08.2017.
 */
class MCTSNode<M : Move> private constructor(private val nodeSelectionPolicy: NodeSelectionPolicy,
                                             private val state: MCTSState<M>) : Cloneable {

    private val children: MutableList<MCTSNode<M>> = mutableListOf()

    private var _visits: Int = 0
    val visits: Int
        get() = _visits

    private var _wins: Double = 0.0
    val wins: Double
        get() = _wins

    val isFullyExpanded: Boolean
        get() = !state.hasMoves() && children.isEmpty()

    val bestMove: MCTSNode<M>
        get() = children.maxBy(MCTSNode<M>::_visits) ?:
                throw IllegalStateException("No moves available. Check isFullyExpanded() before call")

    val result: Result? = state.result

    companion object {
        fun <M : Move> createRootNode(nodeSelectionPolicy: NodeSelectionPolicy, initialState: MCTSState<M>): MCTSNode<M> {
            return MCTSNode(nodeSelectionPolicy, initialState)
        }
    }

    fun monteCarloRound() {
        val path = select().toMutableList()
        var leaf = path[path.size - 1]
        if (leaf.state.hasMoves()) {
            leaf = leaf.expand()
            path.add(leaf)
        }
        val result = leaf.state.playout()
        path.forEach { node -> node.update(result) }
    }

    private fun select(): List<MCTSNode<M>> {
        val path = mutableListOf(this)
        var node = this
        while (!node.state.hasMoves() && !node.children.isEmpty()) {
            node = nodeSelectionPolicy.selectNode(node.children)
            path.add(node)
        }

        return path
    }

    private fun expand(): MCTSNode<M> {
        val move = state.pollRandomMove()
        val newState = state.clone()
        newState.doMove(move)
        newState.switchPlayer()

        val newNode = MCTSNode(nodeSelectionPolicy, newState)
        children.add(newNode)
        return newNode
    }

    private fun update(result: Result?) {
        _visits += 1
        if (result?.get(state.previousPlayerId) == Result.PlayerResult.WIN)
            ++_wins
    }
}