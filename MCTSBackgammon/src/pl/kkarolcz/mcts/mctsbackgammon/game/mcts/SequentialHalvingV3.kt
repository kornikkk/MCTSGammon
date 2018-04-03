package pl.kkarolcz.mcts.mctsbackgammon.game.mcts

import pl.kkarolcz.mcts.Result
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonGamesProgress
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonNode
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonState
import pl.kkarolcz.mcts.node.selectionpolicies.UCTNodeSelectionPolicy
import kotlin.math.log2

class SequentialHalvingV3(progress: BackgammonGamesProgress) : BackgammonMCTS(progress) {

    override fun createRootNode(state: BackgammonState) = BackgammonNode.createRootNode(UCTNodeSelectionPolicy(0.3), state)

    override fun simulate(node: BackgammonNode, simulationsLimit: Int) {
        fullyExpandRootNode(node)
        val possibleMovesCount = node.children.size
        var simulationsLeft = 0
        for (i in 1..2) {
            simulationsLeft = sequentialHalving(node, possibleMovesCount, simulationsLimit)
        }

        for (i in 1..simulationsLeft) {
            progress.newMonteCarloRound()
            node.monteCarloRound()
        }
    }

    private fun fullyExpandRootNode(node: BackgammonNode) {
        while (!node.isFullyExpanded) {
            node.expand()
        }
    }

    /**
     * @return simulations left
     */
    private fun sequentialHalving(node: BackgammonNode, possibleMovesCount: Int, simulationsLimit: Int): Int {
        val children = node.children
        val childrenCount = children.size
        if (childrenCount < 2) {
            return simulationsLimit
        }

        val childBudget = countPlayouts(simulationsLimit, childrenCount, possibleMovesCount)

        val childrenWins = mutableListOf<Pair<Int, BackgammonNode>>()
        children.forEach { child ->
            var wins = 0
            for (i in 1..childBudget) {
                progress.newMonteCarloRound()
                val result = child.playout()
                if (result[node.state.currentPlayer] == Result.PlayerResult.WIN)
                    wins++
            }
            childrenWins.add(Pair(wins, child))
        }
        val childrenByAverage = childrenWins.sortedBy { (wins, _) -> wins }
        childrenByAverage.take(childrenByAverage.size / 2).forEach { (_, child) -> children.remove(child) }

        return simulationsLimit - childrenCount * childBudget
    }

    fun countPlayouts(budget: Int, remainingMoves: Int, possibleMoves: Int): Int =
            (budget / (remainingMoves * Math.ceil(log2(possibleMoves.toDouble())))).toInt()

}