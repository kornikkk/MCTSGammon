package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.BAR_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.BEAR_OFF_INDEX
import java.util.*

/**
 * Created by kkarolcz on 24.08.2017.
 */
class SingleMove private constructor(val oldIndex: Byte, val newIndex: Byte) {
    companion object {
        private val INSTANCES: WeakHashMap<Short, SingleMove> = WeakHashMap()

        fun create(oldIndex: Byte, newIndex: Byte): SingleMove {
            val key = perfectHash(oldIndex, newIndex).toShort()
            var value = INSTANCES[key]
            if (value == null) {
                value = SingleMove(oldIndex, newIndex)
                INSTANCES.put(key, value)
            }
            return value
        }

        private fun perfectHash(oldIndex: Byte, newIndex: Byte): Int = oldIndex.toInt() shl 8 or newIndex.toInt()
    }

    fun reversed() = SingleMove(newIndex, oldIndex)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SingleMove

        if (oldIndex != other.oldIndex) return false
        if (newIndex != other.newIndex) return false

        return true
    }

    fun perfectHash(): Int = perfectHash(oldIndex, newIndex)

    override fun hashCode(): Int = perfectHash(oldIndex, newIndex)

    override fun toString(): String = "(${toString(oldIndex)} -> ${toString(newIndex)})"

    private fun toString(index: Byte): String = when (index) {
        BAR_INDEX -> "BAR"
        BEAR_OFF_INDEX -> "OFF"
        else -> index.toString()
    }

}