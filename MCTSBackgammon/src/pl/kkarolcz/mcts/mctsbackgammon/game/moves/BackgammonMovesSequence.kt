package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import pl.kkarolcz.mcts.MCTSMove

/**
 * Created by kkarolcz on 19.11.2017.
 */
//TODO: Can be changed to array of new indices and start index
data class BackgammonMovesSequence(val moves: List<BackgammonMove>) : MCTSMove {
    constructor(vararg moves: BackgammonMove?) : this(moves.filterNotNull())
}