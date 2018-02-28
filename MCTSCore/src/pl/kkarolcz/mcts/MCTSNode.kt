package pl.kkarolcz.mcts

import pl.kkarolcz.mcts.node.selectionpolicies.NodeSelectionPolicy

/**
 * Created by kkarolcz on 07.08.2017.
 * @param originMove may be null if it's opponent's move or root node
 */
abstract class MCTSNode<Self : MCTSNode<Self, M, T>, M : MCTSMove, T : MCTSTraceableMove.Trace>
protected constructor(private val nodeSelectionPolicy: NodeSelectionPolicy,
                      private val state: MCTSState<M, T>,
                      val originMove: M?) : Cloneable {

    private val _children = ChildNodes<M, T, Self>()
    val children get() = _children.allChildren()

    private var _visits: Int = 0
    val visits: Int
        get() = _visits

    private var _wins: Int = 0
    val wins: Int
        get() = _wins

    val isFullyExpanded: Boolean
        get() = !state.hasUntriedMoves() && _children.isEmpty()

    val result: Result? = state.result

    protected abstract fun newNode(nodeSelectionPolicy: NodeSelectionPolicy, state: MCTSState<M, T>, originMove: M?): Self

    override fun toString() = "$wins / $visits"

    fun getBestMove(trace: MCTSTraceableMove.Trace): Self
            = _children.getChildrenWithTrace(trace).maxBy(MCTSNode<Self, M, T>::_visits) ?:
            throw IllegalStateException("No untriedMoves available. Check isFullyExpanded() before call")

    fun monteCarloRound() {
        val path = selectAndExpand()

        val lastNode = path.last()
        val result = lastNode.state.playout()
        path.forEach { node -> node.update(result) }
    }

    fun findChildNode(state: MCTSState<M, T>): Self? =
            _children.allChildren().find { childNode -> childNode.state == state }

    private fun selectAndExpand(): List<Self> {
        val path = select()
        val leaf = path.last()

        if (!leaf.isFullyExpanded) {
            path.add(leaf.expand())
        }
        return path
    }

    private fun select(): MutableList<Self> {
        this as Self
        val path = mutableListOf<Self>(this)
        var node = this
        while (!node.state.hasUntriedMoves() && node._children.isNotEmpty()) {
            node = nodeSelectionPolicy.selectNode(node._children.allChildren())
            path.add(node)
        }

        return path
    }

    private fun expand(): Self {
        val newState = state.copyForExpanding()
        val traceableMove = state.pollRandomMove()

        newState.doMove(traceableMove.move)
        newState.switchPlayer()

        val newNode = newNode(nodeSelectionPolicy, newState, traceableMove.move)
        _children.add(traceableMove.trace, newNode)
        return newNode
    }

    private fun update(result: Result?) {
        _visits++
        if (result?.get(state.currentPlayer) == Result.PlayerResult.WIN)
            _wins++
    }

    private class ChildNodes<M : MCTSMove, T : MCTSTraceableMove.Trace, N : MCTSNode<N, M, T>> {
        val children = mutableMapOf<MCTSTraceableMove.Trace, MutableList<N>>()

        fun isEmpty(): Boolean = children.isEmpty()

        fun isNotEmpty(): Boolean = children.isNotEmpty()

        fun add(trace: MCTSTraceableMove.Trace, node: N) {
            val childrenWithTrace = children.computeIfAbsent(trace) { mutableListOf() }
            childrenWithTrace.add(node)
        }

        fun getChildrenWithTrace(trace: MCTSTraceableMove.Trace): Collection<N> = children.getOrElse(trace) { emptyList() }

        fun allChildren(): Collection<N> = children.values.flatten()

    }

}