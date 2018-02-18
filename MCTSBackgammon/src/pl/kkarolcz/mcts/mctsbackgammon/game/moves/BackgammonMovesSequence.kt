package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import pl.kkarolcz.mcts.MCTSMove
import java.util.*

/**
 * Created by kkarolcz on 19.11.2017.
 */
//TODO: Can be changed to array of new indices and start index
class BackgammonMovesSequence private constructor(moves: List<BackgammonMove>) : MCTSMove, Cloneable {
    private val _moves = moves.toTypedArray()
    val moves get() = _moves

    companion object {
        private val INSTANCES = WeakHashMap<Long, BackgammonMovesSequence>()

        fun create(vararg moves: BackgammonMove): BackgammonMovesSequence = create(moves.toList())

        fun create(moves: List<BackgammonMove>): BackgammonMovesSequence {
            val key = perfectHash(moves)
            return INSTANCES.computeIfAbsent(key) { BackgammonMovesSequence(moves) }
        }

        private fun perfectHash(moves: List<BackgammonMove>): Long {
            var currentShift = 0
            var hash = 0L
            for (move in moves) {
                hash = hash or move.perfectHash().toLong() shl currentShift
                currentShift += 16
            }
            return hash
        }
    }

    fun length(): Int = _moves.size

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

    override fun toString(): String {
        return "[${_moves.joinToString(", ")}]"
    }

}
