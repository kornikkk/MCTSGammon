package pl.kkarolcz.mcts.mctsbackgammon.game.ai

import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonGamesProgress
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonNode
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonState
import pl.kkarolcz.mcts.node.selectionpolicies.UCTNodeSelectionPolicy
import java.lang.Math.*

class SHOnRootLogarithmicThenMCTSWithUCT(progress: BackgammonGamesProgress) : BackgammonAI(progress), MonteCarloTreeSearch, SequentialHalving {

    override fun createRootNode(state: BackgammonState) = BackgammonNode.createRootNode(UCTNodeSelectionPolicy(0.3), state)

    override fun simulate(node: BackgammonNode, simulationsLimit: Int) {
        fullyExpandNode(node)

        val simulationsLeft = sequentialHalving(node, simulationsLimit, progress,  countHalvings(node.children.size))
        monteCarloTreeSearch(node, simulationsLeft, progress)
    }

    private fun countHalvings(possibleMovesCount: Int): Int = when {
        possibleMovesCount > 2 -> round(log(possibleMovesCount.toDouble())).toInt()
        else -> 0
    }

}