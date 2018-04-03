package pl.kkarolcz.mcts.mctsbackgammon.game.mcts

import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonGamesProgress
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonNode
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonState
import pl.kkarolcz.mcts.node.selectionpolicies.UCTNodeSelectionPolicy

/**
 * Created by kkarolcz on 02.04.2018.
 */
abstract class BackgammonMCTS(protected val progress: BackgammonGamesProgress) {

    abstract fun createRootNode(state: BackgammonState): BackgammonNode
    abstract fun simulate(node: BackgammonNode, simulationsLimit: Int)

}