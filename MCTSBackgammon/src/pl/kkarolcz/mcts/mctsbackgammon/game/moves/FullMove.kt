package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import pl.kkarolcz.mcts.MCTSMove
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import java.util.*

/**
 * Created by kkarolcz on 19.11.2017.
 */
//TODO: Can be changed to array of new indices and start index
class FullMove(private val moves: Array<SingleMove>, val dice: Dice) : MCTSMove, Iterable<SingleMove>, Cloneable {

    override fun iterator(): Iterator<SingleMove> = moves.iterator()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FullMove

        if (!Arrays.equals(moves, other.moves)) return false

        return true
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(moves)
    }

    override fun toString() = "[${moves.joinToString(" -> ")}]\n"

}
