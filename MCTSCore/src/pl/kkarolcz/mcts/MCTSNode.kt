package pl.kkarolcz.mcts

import pl.kkarolcz.mcts.node.selectionpolicies.NodeSelectionPolicy

/**
 * Created by kkarolcz on 07.08.2017.
 * @param origin may be null if it's opponent's move or root node
 */
class MCTSNode<M : MCTSMove> private constructor(private val nodeSelectionPolicy: NodeSelectionPolicy,
                                                 private val state: MCTSState<M>,
                                                 val origin: M?) : Cloneable {

    val children: MutableList<MCTSNode<M>> = mutableListOf()

    private var _visits: Int = 0
    val visits: Int
        get() = _visits

    private var _wins: Int = 0
    val wins: Int
        get() = _wins

    val isFullyExpanded: Boolean
        get() = !state.hasMoves() && children.isEmpty()

    val bestMove: MCTSNode<M>
        get() = children.maxBy(MCTSNode<M>::_visits) ?:
                throw IllegalStateException("No moves available. Check isFullyExpanded() before call")

    val result: Result? = state.result

    companion object {
        fun <M : MCTSMove> createRootNode(nodeSelectionPolicy: NodeSelectionPolicy, initialState: MCTSState<M>): MCTSNode<M> {
            initialState.updatePossibleMoves()
            return MCTSNode(nodeSelectionPolicy, initialState, null)
        }
    }

    override fun toString() = "$wins / $visits"

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

    fun findOrCreateChildNode(state: MCTSState<M>): MCTSNode<M> {
        val node = children.find { childNode -> childNode.state == state }
        if (node != null)
            return node

        state.updatePossibleMoves()
        return MCTSNode(nodeSelectionPolicy, state, null)
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
        //TODO update possible moves??? Do not do that in cloning???

        val newNode = MCTSNode(nodeSelectionPolicy, newState, move)
        children.add(newNode)
        return newNode
    }

    private fun update(result: Result?) {
        _visits += 1
        if (result?.get(state.previousPlayerId) == Result.PlayerResult.WIN)
            ++_wins
    }
}