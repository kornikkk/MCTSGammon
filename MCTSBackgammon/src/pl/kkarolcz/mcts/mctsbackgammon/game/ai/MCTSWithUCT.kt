package pl.kkarolcz.mcts.mctsbackgammon.game.ai

import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonGamesProgress
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonNode
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonState
import pl.kkarolcz.mcts.node.selectionpolicies.UCTNodeSelectionPolicy

/**
 * Created by kkarolcz on 04.04.2018.
 */
class MCTSWithUCT(progress: BackgammonGamesProgress) : BackgammonAI(progress), MonteCarloTreeSearch {

    override fun createRootNode(state: BackgammonState) = BackgammonNode.createRootNode(UCTNodeSelectionPolicy(0.3), state)

    override fun simulate(node: BackgammonNode, simulationsLimit: Int) {
        monteCarloTreeSearch(node, simulationsLimit, progress)
    }

}