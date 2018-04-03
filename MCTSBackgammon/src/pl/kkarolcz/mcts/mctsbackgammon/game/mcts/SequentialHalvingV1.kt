package pl.kkarolcz.mcts.mctsbackgammon.game.mcts

import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonGamesProgress
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonNode
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonState
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.node.selectionpolicies.UCTNodeSelectionPolicy
import java.util.*
import kotlin.math.log2

class SequentialHalvingV1(progress: BackgammonGamesProgress) : BackgammonMCTS(progress) {

    override fun createRootNode(state: BackgammonState) = BackgammonNode.createRootNode(UCTNodeSelectionPolicy(0.3), state)

    override fun simulate(node: BackgammonNode, simulationsLimit: Int) {
        sequentialHalving(node, simulationsLimit)
    }

    private fun sequentialHalving(node: BackgammonNode, budget: Int) {
        while (!node.isFullyExpanded) {
            node.expand()
        }

        val childrenByDice = mutableMapOf<Dice?, MutableList<BackgammonNode>>()
        node.children.groupByTo(childrenByDice) { node.originMove?.dice }

        val diceBudget = budget / childrenByDice.size
        if (diceBudget == 0)
            return

        childrenByDice.values.forEach { children ->
            val childrenCount = children.size
            val childBudget = countPlayouts(diceBudget, childrenCount, childrenCount)

            val childrenAverages = mutableListOf<Pair<Double, BackgammonNode>>()
            children.forEach { child ->
                for (i in 1..maxOf(1, childBudget))
                    child.playout()

                val average = child.wins.toDouble() / child.visits
                childrenAverages.add(Pair(average, child))
            }

            childrenAverages.sortedWith(Comparator { o1, o2 -> o1.first.compareTo(o2.first) })
            val childrenToRemove: Int = childrenAverages.size / 2
            childrenAverages.take(childrenToRemove).forEach { (_, child) ->
                children.remove(child)
                node.children.remove(child)
            }

            val newBudget = (diceBudget - childBudget * childrenCount) / children.size
            if (newBudget > 0) {
                children.forEach { child -> sequentialHalving(child, newBudget) }
            }
        }
    }

    fun countPlayouts(budget: Int, remainingMoves: Int, possibleMoves: Int): Int =
            (budget / (remainingMoves * Math.ceil(log2(possibleMoves.toDouble())))).toInt()

}