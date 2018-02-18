package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import java.util.*

/**
 * Created by kkarolcz on 24.08.2017.
 */
class BackgammonMove private constructor(val oldIndex: Byte, val newIndex: Byte) {
    companion object {
        private val INSTANCES: WeakHashMap<Short, BackgammonMove> = WeakHashMap()

        fun create(oldIndex: Byte, newIndex: Byte): BackgammonMove {
            val key = perfectHash(oldIndex, newIndex).toShort()
            var value = INSTANCES[key]
            if (value == null) {
                value = BackgammonMove(oldIndex, newIndex)
                INSTANCES.put(key, value)
            }
            return value
        }

        private fun perfectHash(oldIndex: Byte, newIndex: Byte): Int = oldIndex.toInt() shl 8 or newIndex.toInt()
    }

    fun reversed() = BackgammonMove(newIndex, oldIndex)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BackgammonMove

        if (oldIndex != other.oldIndex) return false
        if (newIndex != other.newIndex) return false

        return true
    }

    fun perfectHash(): Int = perfectHash(oldIndex, newIndex)

    override fun hashCode(): Int = perfectHash(oldIndex, newIndex)

    override fun toString(): String = "($oldIndex -> $newIndex)"

}