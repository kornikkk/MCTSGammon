package pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random.dice4

import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMovesBuilder
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.PossibleMovesDoubling
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random.FullMovesType
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random.RandomFullMovesDoublingUtils.randomFromPartial
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random.RandomFullMovesDoublingUtils.randomFromSequence
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random.RandomFullMovesDoublingUtils.randomFromSequenceAndPartial
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random.RandomFullMovesDoublingUtils.randomFromSequences
import pl.kkarolcz.utils.binomialCoefficient

/**
 * Created by kkarolcz on 28.03.2018.
 */
enum class FullMoveTypeDice4Possible4 : FullMovesType {
    PARTIAL_4 {

        override fun count(possibleMoves: PossibleMovesDoubling): Int =
                when {
                    possibleMoves.partialMoves.size < 4 -> 0
                    else -> binomialCoefficient(possibleMoves.partialMoves.size, 4)
                }

        override fun randomMove(builder: FullMovesBuilder, possibleMoves: PossibleMovesDoubling): FullMove =
                randomFromPartial(builder, possibleMoves.partialMoves, 4)

    },
    SEQUENCE_3 {

        override fun count(possibleMoves: PossibleMovesDoubling): Int =
                possibleMoves.sequencesWith3.size

        override fun randomMove(builder: FullMovesBuilder, possibleMoves: PossibleMovesDoubling): FullMove =
                randomFromSequence(builder, possibleMoves.sequencesWith3, 3)

    },
    SEQUENCE_2_WITH_PARTIAL_1 {

        override fun count(possibleMoves: PossibleMovesDoubling): Int =
                when {
                    possibleMoves.sequencesWith2.size < 1 -> 0
                    else -> (possibleMoves.sequencesWith2.size - 1) * possibleMoves.partialMoves.size
                }

        override fun randomMove(builder: FullMovesBuilder, possibleMoves: PossibleMovesDoubling): FullMove =
                randomFromSequenceAndPartial(builder, possibleMoves.sequencesWith2, possibleMoves.partialMoves, 2, 1)

    },
    SEQUENCE_1_WITH_SEQUENCE_1 {

        override fun count(possibleMoves: PossibleMovesDoubling): Int =
                when {
                    possibleMoves.sequencesWith1.size < 2 -> 0
                    else -> binomialCoefficient(possibleMoves.sequencesWith1.size, 2)
                }

        override fun randomMove(builder: FullMovesBuilder, possibleMoves: PossibleMovesDoubling): FullMove =
                randomFromSequences(builder, possibleMoves.sequencesWith1, possibleMoves.sequencesWith1, 1, 1)

    },
    SEQUENCE_1_WITH_PARTIAL_2 {

        override fun count(possibleMoves: PossibleMovesDoubling): Int =
                when {
                    possibleMoves.sequencesWith1.size < 2 -> 0
                    else -> (possibleMoves.sequencesWith1.size - 2) * binomialCoefficient(possibleMoves.partialMoves.size, 2)
                }

        override fun randomMove(builder: FullMovesBuilder, possibleMoves: PossibleMovesDoubling): FullMove =
                randomFromSequenceAndPartial(builder, possibleMoves.sequencesWith1, possibleMoves.partialMoves, 1, 2)

    };

}