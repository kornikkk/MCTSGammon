package pl.kkarolcz.mcts

import pl.kkarolcz.mcts.node.selectionpolicies.NodeSelectionPolicy

/**
 * Created by kkarolcz on 07.08.2017.
 * @param originMove may be null if it's opponent's move or root node
 */

@Suppress("UNCHECKED_CAST")
abstract class MCTSNode<Self : MCTSNode<Self, S, M>, S : MCTSState<M>, M : MCTSMove>
protected constructor(private val nodeSelectionPolicy: NodeSelectionPolicy, protected val state: S, val originMove: M?) : Cloneable {

    val children = mutableListOf<Self>()

    private var _visits: Int = 0
    val visits: Int get() = _visits

    private var _wins: Int = 0
    val wins: Int get() = _wins

    val isFullyExpanded get() = !state.hasUntriedMoves()

    val result: Result? get() = state.result

    protected abstract fun newNode(nodeSelectionPolicy: NodeSelectionPolicy, state: S, originMove: M?): Self

    override fun toString() = "$wins / $visits"

    //TODO wins to visits
    fun getBestMove(): Self =
            children.maxBy { child -> child._wins.toDouble() / child._visits } ?: throw IllegalStateException("No child nodes")

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
        val move = state.pollRandomMove()

        newState.doMove(move)
        newState.switchPlayer()

        val newNode = newNode(nodeSelectionPolicy, newState, move)
        children.add(newNode)
        return newNode
    }

    private fun update(result: Result?) {
        //TODO: Check what happens when we decrement lost games
        _visits++
        if (result?.get(state.currentPlayer.opponent()) == Result.PlayerResult.WIN)
            _wins++
    }

}