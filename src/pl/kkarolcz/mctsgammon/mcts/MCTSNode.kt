package pl.kkarolcz.mctsgammon.mcts

import pl.kkarolcz.mctsgammon.utils.randomElement

/**
 * Created by kkarolcz on 07.08.2017.
 */
class MCTSNode<S : Move>(val state: State<S>, val parent: MCTSNode<S>? = null) : Cloneable {
    private val children: MutableList<MCTSNode<S>> = mutableListOf()

    private var _visits: Double = 0.0
    val visits: Double
        get() = _visits

    private var _wins: Double = 0.0
    val wins: Double
        get() = _wins

    val isFullyExpanded: Boolean
        get() = !state.hasMoves() || children.isEmpty() // TODO: ???

    val bestMove: MCTSNode<S>?
        get() = children.maxBy { node -> node.visits }


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

    private fun select(): List<MCTSNode<S>> {
        val path = mutableListOf(this)
        var node = this
        while (!node.state.hasMoves() && !node.children.isEmpty()) {
            node = node.children.randomElement()
            path.add(node)
        }

        return path
    }

    private fun expand(): MCTSNode<S> {
        val move = state.pollRandomMove()
        val newState = state.clone()
        newState.doMove(move)

        val newNode = MCTSNode(newState, this)
        children.add(newNode)
        return newNode
    }

    fun update(result: Result?) {
        _visits += 1
        if (result?.get(state.currentPlayerId) == Result.PlayerResult.WIN)
            ++_wins
    }
}