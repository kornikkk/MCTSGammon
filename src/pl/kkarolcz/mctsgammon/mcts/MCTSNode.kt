package pl.kkarolcz.mctsgammon.mcts

/**
 * Created by kkarolcz on 07.08.2017.
 */
class MCTSNode<T : Move>(val parent: MCTSNode<T>? = null, val state: State<T>) : Cloneable {
    private val children: MutableList<MCTSNode<T>> = mutableListOf()

    private var _visits: Double = 0.0
    val visits: Double
        get() = _visits

    private var _wins: Double = 0.0
    val wins: Double
        get() = _wins

    val isFullyExpanded: Boolean
        get() = !state.hasMoves() || children.isEmpty() // TODO: ???

    val bestMove: MCTSNode<T>?
        get() = children.maxBy { node -> node.visits }


    fun monteCarloRound() {
        val path = select().toMutableList()
        var leaf = path[path.size - 1]
        if (leaf.state.hasMoves()) {
            leaf = leaf.expand()
            path.add(leaf)
        }
        val result = leaf.state.rollout()
        path.forEach { node -> node.update(result) }
    }

    private fun select(): List<MCTSNode<T>> {
        val path = mutableListOf<MCTSNode<T>>()
        var node = this
        while (!node.state.hasMoves() && !node.children.isEmpty()) {
            node = node.children.randomElement()
            path.add(node)
        }

        return path
    }

    private fun expand(): MCTSNode<T> {
        val move = state.pollRandomMove()
        val newState = state.clone()
        newState.doMove(move)

        val newNode = MCTSNode(this, newState)
        children.add(newNode)
        return newNode
    }

    fun addChild(child: MCTSNode<T>) {
        children.add(child)
    }

    fun update(result: Result) {
        _visits += 1
        _wins += result[state.previousPlayerId]
    }
}