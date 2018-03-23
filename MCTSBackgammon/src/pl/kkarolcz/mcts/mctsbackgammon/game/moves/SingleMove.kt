package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.BAR_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.BEAR_OFF_INDEX

/**
 * Created by kkarolcz on 24.08.2017.
 */
class SingleMove constructor(val oldIndex: Byte, val newIndex: Byte) : Cloneable {

    fun reversed() = SingleMove(newIndex, oldIndex)

    public override fun clone(): SingleMove = SingleMove(oldIndex, newIndex)

    fun isBearingOff(): Boolean = newIndex == BoardIndex.BEAR_OFF_INDEX

    override fun toString(): String = "(${toString(oldIndex)} -> ${toString(newIndex)})"

    private fun toString(index: Byte): String = when (index) {
        BAR_INDEX -> "BAR"
        BEAR_OFF_INDEX -> "OFF"
        else -> index.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SingleMove

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