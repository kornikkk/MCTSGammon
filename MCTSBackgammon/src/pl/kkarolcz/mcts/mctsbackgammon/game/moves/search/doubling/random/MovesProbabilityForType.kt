package pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random

import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.PossibleMovesDoubling
import pl.kkarolcz.utils.weightedRandom

/**
 * Created by kkarolcz on 29.03.2018.
 */

class MovesProbabilityForType<E>(possibleMoves: PossibleMovesDoubling, private val fullMoveType: Class<E>)
        where E : Enum<E>?, E : FullMovesType {

    private val weights: Array<Int> = fullMoveType.enumConstants.map { it.count(possibleMoves) }.toTypedArray()

    val movePossible = weights.sum() > 0

    fun randomMoveType(): E = weightedRandom(weights, fullMoveType.enumConstants)
            ?: throw IllegalArgumentException("Weights cannot sum to zero")
}
