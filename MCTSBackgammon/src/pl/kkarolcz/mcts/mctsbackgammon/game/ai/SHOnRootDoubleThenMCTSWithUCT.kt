package pl.kkarolcz.mcts.mctsbackgammon.game.ai

import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonGamesProgress
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonNode
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonState
import pl.kkarolcz.mcts.node.selectionpolicies.UCTNodeSelectionPolicy

class SHOnRootDoubleThenMCTSWithUCT(progress: BackgammonGamesProgress) : BackgammonAI(progress), MonteCarloTreeSearch, SequentialHalving {

    override fun createRootNode(state: BackgammonState) = BackgammonNode.createRootNode(UCTNodeSelectionPolicy(0.3), state)

    override fun simulate(node: BackgammonNode, simulationsLimit: Int) {
        val simulationsLeft = sequentialHalving(node, simulationsLimit, progress, 2)
        monteCarloTreeSearch(node, simulationsLeft, progress)
    }
}