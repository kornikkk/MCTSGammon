package pl.kkarolcz.mcts.mctsbackgammon.game.ai

import pl.kkarolcz.mcts.Result
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonGamesProgress
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonNode
import java.util.*
import kotlin.math.log2

/**
 * Created by kkarolcz on 04.04.2018.
 */
interface SequentialHalving {

    /**
     * @param maxHalvings Maximum number of halvings on node. -1 is unlimited
     * @return Simulations left
     */
    fun sequentialHalving(node: BackgammonNode, simulationsLimit: Int, progress: BackgammonGamesProgress, maxHalvings: Int = -1): Int {
        fullyExpandNode(node)
        val possibleMovesCount = node.children.size

        var simulationsLeft = simulationsLimit
        var halvings = 0

        while (simulationsLeft > 0 && node.children.size > 1 && (maxHalvings == -1 || halvings < maxHalvings)) {
            simulationsLeft = sequentialHalving(node, possibleMovesCount, simulationsLeft, progress)
            halvings++
        }

        return simulationsLeft
    }

    /**
     * @return simulations left
     */
    private fun sequentialHalving(node: BackgammonNode, possibleMovesCount: Int, simulationsLeft: Int, progress: BackgammonGamesProgress): Int {
        fullyExpandNode(node)

        val children = node.children
        val childrenCount = children.size
        if (childrenCount == 1) {
            return simulationsLeft
        }

        val childBudget = countPlayouts(simulationsLeft, childrenCount, possibleMovesCount)

        val childrenWins = Collections.synchronizedList(mutableListOf<Pair<Int, BackgammonNode>>())
        children.parallelStream().forEach { child ->
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

        return simulationsLeft - childrenCount * childBudget
    }

    fun fullyExpandNode(node: BackgammonNode) {
        while (!node.isFullyExpanded) {
            node.expand()
        }
    }

    private fun countPlayouts(budget: Int, remainingMoves: Int, possibleMoves: Int): Int =
            (budget / (remainingMoves * Math.ceil(log2(possibleMoves.toDouble())))).toInt()

}