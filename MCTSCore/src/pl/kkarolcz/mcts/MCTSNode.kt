package pl.kkarolcz.mcts

import pl.kkarolcz.mcts.node.selectionpolicies.NodeSelectionPolicy

/**
 * Created by kkarolcz on 07.08.2017.
 * @param originMove may be null if it's opponent's move or root node
 */

@Suppress("UNCHECKED_CAST")
abstract class MCTSNode<Self : MCTSNode<Self, S, M>, S : MCTSState<M>, M : MCTSMove>
protected constructor(private val nodeSelectionPolicy: NodeSelectionPolicy, protected val state: S, val originMove: M?) : Cloneable {

    var visits: Int = 0
        private set

    var wins: Int = 0
        private set

    val children: MutableList<Self> = mutableListOf()

    val bestNode: Self
        get() = children.maxBy { child -> child.wins.toDouble() / child.visits } ?: throw IllegalStateException("No child nodes")

    val isFullyExpanded: Boolean get() = !state.hasUntriedMoves()

    val result: Result? get() = state.result

    override fun toString() = "$wins / $visits"

    fun monteCarloRound() {
        val path = select()
        val leaf = path.last()

        if (!leaf.isFullyExpanded) {
            path.add(leaf.expand())
        }

        val lastNode = path.last()
        val result = lastNode.state.playout()
        path.forEach { node -> node.update(result) }
    }

    fun findChildNode(state: S): Self? = children.find { childNode -> childNode.state == state }

    protected abstract fun newNode(nodeSelectionPolicy: NodeSelectionPolicy, state: S, originMove: M?): Self

    private fun select(): MutableList<Self> {
        var node = this as Self
        val path = mutableListOf(node)

        while (!node.state.hasUntriedMoves() && node.children.isNotEmpty()) {
            node = nodeSelectionPolicy.selectChildNode(node)
            path.add(node)
        }

        return path
    }

    private fun expand(): Self {
        val newState = state.copyForExpanding() as S
        val move = state.pollRandomUntriedMove()

        newState.doMove(move)
        newState.switchPlayer()

        val newNode = newNode(nodeSelectionPolicy, newState, move)
        children.add(newNode)
        return newNode
    }

    private fun update(result: Result?) {
        //TODO: Check what happens when we decrement lost games
        visits++
        if (result?.get(state.currentPlayer.opponent()) == Result.PlayerResult.WIN)
            wins++
    }

}