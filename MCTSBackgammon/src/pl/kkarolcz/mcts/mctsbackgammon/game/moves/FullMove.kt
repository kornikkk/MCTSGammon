package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import pl.kkarolcz.mcts.MCTSMove
import java.util.*

/**
 * Created by kkarolcz on 19.11.2017.
 */
//TODO: Can be changed to array of new indices and start index
class FullMove private constructor(moves: List<SingleMove>) : MCTSMove, Cloneable {
    private val _moves = moves.toTypedArray()
    val moves get() = _moves

    companion object {
        fun create(vararg moves: SingleMove): FullMove = create(moves.toList())

        fun create(moves: List<SingleMove>): FullMove {
            return FullMove(moves)
        }
    }

    fun length(): Int = _moves.size

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FullMove

        if (!Arrays.equals(_moves, other._moves)) return false

        return true
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(_moves)
    }

    override fun toString(): String {
        return "[${_moves.joinToString(", ")}]"
    }

}
