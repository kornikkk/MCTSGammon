package pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random

import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMovesBuilder
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.SingleMove
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.SequencesForPartialMoves

/**
 * Created by kkarolcz on 30.03.2018.
 */
object RandomFullMovesDoublingUtils {

    fun randomFromPartial(builder: FullMovesBuilder, partialMoves: List<SingleMove>, partialCount: Int): FullMove {
        checkLength(builder, partialCount)

        return builder.append(randomPartialMoves(partialMoves, partialCount)).build()
    }

    fun randomFromBarSequence(builder: FullMovesBuilder, barSequences: SequencesForPartialMoves, sequenceLength: Int): FullMove {
        checkLength(builder, sequenceLength)

        builder.append(barSequences.randomSequence().trimmed(sequenceLength))
        return builder.build()
    }

    fun randomFromBarSequenceAndPartial(builder: FullMovesBuilder, barSequences: SequencesForPartialMoves, partialMoves: List<SingleMove>,
                                        sequenceLength: Int, partialCount: Int): FullMove {
        checkLength(builder, sequenceLength + partialCount)

        val randomPartialMoves = randomPartialMoves(partialMoves, partialCount)
        builder.append(randomPartialMoves)
        builder.append(barSequences.randomSequence().trimmed(sequenceLength))

        return builder.build()
    }

    fun randomFromBarSequenceAndSequence(builder: FullMovesBuilder, barSequences: SequencesForPartialMoves,
                                         sequences: SequencesForPartialMoves, barSequenceLength: Int, sequenceLength: Int): FullMove {
        checkLength(builder, barSequenceLength + sequenceLength + 1)

        builder.append(barSequences.randomSequence().trimmed(barSequenceLength))

        val randomSequence = sequences.randomSequence()
        builder.append(randomSequence.partialMove)
        builder.append(randomSequence.trimmed(sequenceLength))

        return builder.build()
    }

    fun randomFromBarSequences(builder: FullMovesBuilder, barSequences1: SequencesForPartialMoves, barSequences2: SequencesForPartialMoves,
                               barSequence1Length: Int, barSequence2Length: Int): FullMove {
        checkLength(builder, barSequence1Length + barSequence2Length)

        builder.append(barSequences1.randomSequence().trimmed(barSequence1Length))
        builder.append(barSequences2.randomSequence().trimmed(barSequence2Length))

        return builder.build()
    }

    fun randomFromSequence(builder: FullMovesBuilder, sequences: SequencesForPartialMoves, sequenceLength: Int): FullMove {
        checkLength(builder, sequenceLength + 1)

        val randomSequence = sequences.randomSequence()
        builder.append(randomSequence.partialMove)
        builder.append(randomSequence.trimmed(sequenceLength))
        return builder.build()
    }

    fun randomFromSequenceAndPartial(builder: FullMovesBuilder, sequences: SequencesForPartialMoves, partialMoves: List<SingleMove>,
                                     sequenceLength: Int, partialCount: Int): FullMove {
        checkLength(builder, sequenceLength + 1 + partialCount)

        val randomPartialMoves = randomPartialMoves(partialMoves, partialCount)
        builder.append(randomPartialMoves)

        val randomSequence = sequences.filteredByNotContains(randomPartialMoves).randomSequence()
        builder.append(randomSequence.partialMove)
        builder.append(randomSequence.trimmed(sequenceLength))

        return builder.build()
    }

    fun randomFromSequences(builder: FullMovesBuilder, sequences1: SequencesForPartialMoves, sequences2: SequencesForPartialMoves,
                            sequence1Length: Int, sequence2Length: Int): FullMove {
        checkLength(builder, sequence1Length + 1 + sequence2Length + 1)

        val randomSequence1 = sequences1.randomSequence()
        builder.append(randomSequence1.partialMove)
        builder.append(randomSequence1.trimmed(sequence1Length))

        val randomSequence2 = sequences2.filteredByNotContains(randomSequence1.partialMove).randomSequence()
        builder.append(randomSequence2.partialMove)
        builder.append(randomSequence2.trimmed(sequence2Length))

        return builder.build()
    }

    private fun checkLength(builder: FullMovesBuilder, appendedCount: Int) {
        if (builder.length + appendedCount > 4) throw IllegalArgumentException("Full move cannot be longer than 4")
    }

    private fun randomPartialMoves(partialMoves: List<SingleMove>, count: Int) = partialMoves.shuffled().subList(0, count)

}