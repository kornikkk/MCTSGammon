package pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.playout

import pl.kkarolcz.utils.weightedRandom

/**
 * Created by kkarolcz on 29.03.2018.
 */

class MovesProbabilityForType<E>(possibleMoves: PossibleMoves, private val fullMoveTypes: Class<E>)
        where E : Enum<E>?, E : FullMovesType {

    private val weights: Array<Int> = fullMoveTypes.enumConstants.map { it.count(possibleMoves) }.toTypedArray()

    val movePossible = weights.sum() > 0

    fun randomMoveType(): E = weightedRandom(weights, fullMoveTypes.enumConstants)
            ?: throw IllegalArgumentException("Weights cannot sum to zero")
}
