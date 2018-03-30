package pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling

import org.apache.commons.collections4.CollectionUtils
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
            for (partialMove in partialMoves) {
                if (key !== partialMove) {
                    filteredSequences.map[key] = value
                }
            }
        }
        return filteredSequences
    }

    fun containsAnyMove(moves: Collection<SingleMove>) = CollectionUtils.containsAny(map.keys, moves)

    fun randomSequence(): SequenceForPartialMove = map.values.toList().randomElement()
}