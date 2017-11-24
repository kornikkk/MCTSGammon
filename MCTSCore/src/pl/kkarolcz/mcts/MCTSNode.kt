package pl.kkarolcz.mcts

import pl.kkarolcz.mcts.node.selectionpolicies.NodeSelectionPolicy

/**
 * Created by kkarolcz on 07.08.2017.
 * @param originMove may be null if it's opponent's move or root node
 */
class MCTSNode<M : MCTSMove> private constructor(private val nodeSelectionPolicy: NodeSelectionPolicy,
                                                 private val state: MCTSState<M>,
                                                 val originMove: M?) : Cloneable {

    val children: MutableList<MCTSNode<M>> = mutableListOf()

    private var _visits: Int = 0
    val visits: Int
        get() = _visits

    private var _wins: Int = 0
    val wins: Int
        get() = _wins

    val isFullyExpanded: Boolean
        get() = !state.hasUntriedMoves() && children.isEmpty()

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
        val path = selectAndExpand()

        val lastNode = path.last()
        val result = lastNode.state.playout()
        path.forEach { node -> node.update(result) }
    }
    fun findOrCreateChildNode(state: MCTSState<M>): MCTSNode<M> {
        val node = children.find { childNode -> childNode.state == state }
        if (node != null)
            return node

        state.updatePossibleMoves()
        return MCTSNode(nodeSelectionPolicy, state, null)
    }

    private fun selectAndExpand(): List<MCTSNode<M>> {
        val path = select()
        val leaf = path.last()

        if (!leaf.isFullyExpanded) {
            path.add(leaf.expand())
        }
        return path
    }

    private fun select(): MutableList<MCTSNode<M>> {
        val path = mutableListOf(this)
        var node = this
        while (!node.state.hasUntriedMoves() && node.children.isNotEmpty()) {
            node = nodeSelectionPolicy.selectNode(node.children)
            path.add(node)
        }

        return path
    }

    private fun expand(): MCTSNode<M> {
        val newState = state.clone()
        val move = state.pollRandomMove()

        if (move != null) {
            newState.doMove(move)
        }

        newState.switchPlayer()

        val newNode = MCTSNode(nodeSelectionPolicy, newState, move)
        children.add(newNode)
        return newNode
    }

    private fun update(result: Result?) {
        _visits++
        if (result?.get(state.currentPlayerId) == Result.PlayerResult.WIN)
            _wins++
    }

}