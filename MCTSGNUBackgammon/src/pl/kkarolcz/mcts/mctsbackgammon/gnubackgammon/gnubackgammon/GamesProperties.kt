package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon

import pl.kkarolcz.mcts.mctsbackgammon.game.ai.BackgammonAIType
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gnubackgammon.difficulty.GNUBackgammonDifficulty

/**
 * Created by kkarolcz on 23.03.2018.
 */
data class GamesProperties(val simulationsLimit: Int, val numberOfGames: Int, val difficulty: GNUBackgammonDifficulty,
                           val backgammonAIType: BackgammonAIType)