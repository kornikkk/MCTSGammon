package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import pl.kkarolcz.mcts.MCTSMove
import java.util.*

/**
 * Created by kkarolcz on 19.11.2017.
 */
//TODO: Can be changed to array of new indices and start index
class BackgammonMovesSequence(moves: List<BackgammonMove>) : MCTSMove, Cloneable {
    private val _moves = moves.toTypedArray()
    val moves get() = _moves

    constructor(vararg moves: BackgammonMove?) : this(moves.filterNotNull())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BackgammonMovesSequence

        if (!Arrays.equals(_moves, other._moves)) return false

        return true
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(_moves)
    }


}