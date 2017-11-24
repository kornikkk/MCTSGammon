package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import pl.kkarolcz.mcts.MCTSMove

/**
 * Created by kkarolcz on 24.08.2017.
 */
@Deprecated("remove that")
class OLD_BackgammonMovesSequence(val singleMoves: List<OLD_SingleBackgammonMove>) : MCTSMove {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OLD_BackgammonMovesSequence

        if (singleMoves != other.singleMoves) return false

        return true
    }

    override fun hashCode(): Int {
        return singleMoves.hashCode()
    }
}