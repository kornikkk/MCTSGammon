package pl.kkarolcz.mcts.mctsbackgammon.game.dices

import pl.kkarolcz.mcts.MCTSTraceableMove
import java.util.*

/**
 * Created by kkarolcz on 24.08.2017.
 */
class Dice(value1: Byte, value2: Byte) : MCTSTraceableMove.Trace {
    val first: Byte = maxOf(value1, value2)
    val second: Byte = minOf(value1, value2)

    val doubling = first == second


    companion object {
        val PERMUTATIONS = listOf(
                Dice(1, 1), Dice(1, 2), Dice(1, 3), Dice(1, 4), Dice(1, 5), Dice(1, 6),
                Dice(2, 2), Dice(2, 3), Dice(2, 4), Dice(2, 5), Dice(2, 6),
                Dice(3, 3), Dice(3, 4), Dice(3, 5), Dice(3, 6),
                Dice(4, 4), Dice(4, 5), Dice(4, 6),
                Dice(5, 5), Dice(5, 6),
                Dice(6, 6)
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Dice

        if (first != other.first) return false
        if (second != other.second) return false

        return true
    }

    override fun hashCode(): Int = Objects.hash(first, second)

}
