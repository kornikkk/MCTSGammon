package pl.kkarolcz.mcts

import pl.kkarolcz.mcts.node.selectionpolicies.NodeSelectionPolicy

/**
 * Created by kkarolcz on 07.08.2017.
 * @param originMove may be null if it's opponent's move or root node
 */

@Suppress("UNCHECKED_CAST")
abstract class MCTSNode<Self : MCTSNode<Self, S, M>, S : MCTSState<M>, M : MCTSMove>
protected constructor(private val nodeSelectionPolicy: NodeSelectionPolicy,
                      val state: S,
                      var parent: Self?,
                      val originMove: M?) : Cloneable {

    var visits: Int = 0
        private set

    var wins: Int = 0
        private set

    val children: MutableList<Self> = mutableListOf()

    val bestNode: Self
        get() {
            val bestChild = children.maxBy { child -> child.wins.toDouble() / child.visits }
            when {
                bestChild != null -> return bestChild
                else -> throw IllegalStateException("No child nodes")
            }
        }

    val isFullyExpanded: Boolean get() = !state.hasUntriedMoves()

    val result: Result? get() = state.result


    override fun toString() = "$wins / $visits"

    fun monteCarloRound() {
        var node = select()

        if (!node.isFullyExpanded) {
            node = node.expand()
        }

        node.playout()
    }

    fun findChildNode(state: S): Self? = children.find { childNode -> childNode.state == state }

    protected abstract fun newNode(nodeSelectionPolicy: NodeSelectionPolicy, state: S, originMove: M?): Self

    private fun select(): Self {
        var node = this as Self
        while (!node.state.hasUntriedMoves() && node.children.isNotEmpty()) {
            node = nodeSelectionPolicy.selectChildNode(node)
        }

        return node
    }

    fun expand(): Self {
        val newState = state.copyForExpanding() as S
        val move = state.pollRandomUntriedMove()

        newState.doMove(move)
        newState.switchPlayer()

        val newNode = newNode(nodeSelectionPolicy, newState, move)
        children.add(newNode)
        return newNode
    }

    fun playout(): Result {
        val result = state.playout()
        update(result)
        return result
    }

    private fun update(result: Result) {
        var node: Self? = this as Self
        while (node != null) {
            node.visits++

            val opponentResult = result[node.state.currentPlayer.opponent()]
            when (opponentResult) {
                Result.PlayerResult.WIN -> node.wins++
                Result.PlayerResult.LOSE -> node.wins--
            }

            node = node.parent
        }
    }

}

