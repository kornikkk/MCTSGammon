package pl.kkarolcz.mcts.mctsbackgammon.game.ai

import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonGamesProgress
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonNode

interface MonteCarloTreeSearch {

    fun monteCarloTreeSearch(node: BackgammonNode, simulationsLimit: Int, progress: BackgammonGamesProgress) {
        for (i in 1..simulationsLimit) {
            progress.newMonteCarloRound()
            node.monteCarloRound()
        }
    }

}