package pl.kkarolcz.mcts.mctsbackgammon.game.mcts

import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonGamesProgress
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonNode
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonState
import pl.kkarolcz.mcts.node.selectionpolicies.UCTNodeSelectionPolicy

class ClassicMCTSWithUCT(progress: BackgammonGamesProgress) : BackgammonMCTS(progress) {

    override fun createRootNode(state: BackgammonState) = BackgammonNode.createRootNode(UCTNodeSelectionPolicy(0.3), state)

    override fun simulate(node: BackgammonNode, simulationsLimit: Int) {
        for (i in 1..simulationsLimit) {
            progress.newMonteCarloRound()
            node.monteCarloRound()
        }
    }

}