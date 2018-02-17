package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import com.carrotsearch.hppc.ByteObjectHashMap
import com.carrotsearch.hppc.ByteObjectMap

/**
 * Created by kkarolcz on 24.08.2017.
 */
class BackgammonMove private constructor(val oldIndex: Byte, val newIndex: Byte) {
    companion object {
        private val INSTANCES: ByteObjectMap<ByteObjectMap<BackgammonMove>> = ByteObjectHashMap()

        fun create(oldIndex: Byte, newIndex: Byte): BackgammonMove {
            val forOldIndex: ByteObjectMap<BackgammonMove>
            if (!INSTANCES.containsKey(oldIndex)) {
                forOldIndex = ByteObjectHashMap()
                INSTANCES.put(oldIndex, forOldIndex)
            } else {
                forOldIndex = INSTANCES.get(oldIndex)
            }

            if (!forOldIndex.containsKey(newIndex)) {
                val move = BackgammonMove(oldIndex, newIndex)
                forOldIndex.put(newIndex, move)
                return move
            }
            return forOldIndex.get(newIndex)
        }
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

    override fun hashCode(): Int {
        var result = oldIndex.toInt()
        result = 31 * result + newIndex
        return result
    }
}