package pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling

import pl.kkarolcz.mcts.mctsbackgammon.game.moves.SingleMove
import pl.kkarolcz.utils.randomElement
import java.util.*

class SequencesForPartialMoves {
    private val map = IdentityHashMap<SingleMove, SequenceForPartialMove>(15)

    val size get() = map.size

    fun getOrCreateSequence(partialMove: SingleMove): SequenceForPartialMove =
            map.computeIfAbsent(partialMove) { SequenceForPartialMove(partialMove) }

    fun getSequence(partialMove: SingleMove): SequenceForPartialMove? = map.getOrDefault(partialMove, null)

    fun filteredByMinLength(minLength: Int): SequencesForPartialMoves {
        val filteredSequences = SequencesForPartialMoves()
        for ((key, value) in map) {
            if (value.length >= minLength)
                filteredSequences.map[key] = value
        }
        return filteredSequences
    }

    fun filteredByNotContains(partialMove: SingleMove) = filteredByNotContains(Collections.singleton(partialMove))

    fun filteredByNotContains(partialMoves: Collection<SingleMove>): SequencesForPartialMoves {
        val filteredSequences = SequencesForPartialMoves()
        for ((key, value) in map) {
            if (!containsMove(partialMoves, key)) {
                filteredSequences.map[key] = value
            }
        }
        return filteredSequences
    }

    private fun containsMove(partialMoves: Collection<SingleMove>, move: SingleMove): Boolean {
        for (partialMove in partialMoves) {
            if (move === partialMove) return true
        }
        return false
    }

    fun randomSequence(): SequenceForPartialMove = map.values.toList().randomElement()
}