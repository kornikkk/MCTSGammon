package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import pl.kkarolcz.mcts.MCTSMove

/**
 * Created by kkarolcz on 24.08.2017.
 */
class BackgammonMovesSequence(val singleMoves: List<SingleBackgammonMove>) : MCTSMove {

    companion object {
        fun notPossibleMove() = BackgammonMovesSequence(emptyList())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BackgammonMovesSequence

        if (singleMoves != other.singleMoves) return false

        return true
    }

    override fun hashCode(): Int {
        return singleMoves.hashCode()
    }
}