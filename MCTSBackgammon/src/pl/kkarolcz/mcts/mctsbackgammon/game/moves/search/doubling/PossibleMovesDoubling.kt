package pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling

import pl.kkarolcz.mcts.mctsbackgammon.game.moves.SingleMove

/**
 * Created by kkarolcz on 29.03.2018.
 */
class PossibleMovesDoubling {
    var barMove: SingleMove? = null

    val barSequentialMoves = SequencesForPartialMoves()
    val barSequencesWith1 get() = barSequentialMoves.filteredByMinLength(1)
    val barSequencesWith2 get() = barSequentialMoves.filteredByMinLength(2)
    val barSequencesWith3 get() = barSequentialMoves.filteredByMinLength(3)

    val partialMoves = mutableListOf<SingleMove>()

    val standardSequentialMoves = SequencesForPartialMoves()
    val sequencesWith1 get() = standardSequentialMoves.filteredByMinLength(1)
    val sequencesWith2 get() = standardSequentialMoves.filteredByMinLength(2)
    val sequencesWith3 get() = standardSequentialMoves.filteredByMinLength(3)


    var bearOffPossibleInitially = false
    var bearOffPossibleConditionally = false

}