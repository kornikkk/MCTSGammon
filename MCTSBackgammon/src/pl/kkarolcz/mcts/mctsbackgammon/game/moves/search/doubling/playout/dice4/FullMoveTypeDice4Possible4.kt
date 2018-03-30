package pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.playout.dice4

import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMovesBuilder
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.playout.FullMovesType
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.playout.PossibleMoves
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.playout.RandomMovesHelper.randomFromPartial
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.playout.RandomMovesHelper.randomFromSequence
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.playout.RandomMovesHelper.randomFromSequenceAndPartial
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.playout.RandomMovesHelper.randomFromSequences
import pl.kkarolcz.utils.binomialCoefficient

/**
 * Created by kkarolcz on 28.03.2018.
 */
enum class FullMoveTypeDice4Possible4 : FullMovesType {
    PARTIAL_4 {

        override fun count(possibleMoves: PossibleMoves): Int =
                when {
                    possibleMoves.partialMoves.size < 4 -> 0
                    else -> binomialCoefficient(possibleMoves.partialMoves.size, 4)
                }

        override fun randomMove(builder: FullMovesBuilder, possibleMoves: PossibleMoves): FullMove =
                randomFromPartial(builder, possibleMoves.partialMoves, 4)

    },
    SEQUENCE_3 {

        override fun count(possibleMoves: PossibleMoves): Int =
                possibleMoves.sequencesWith3.size

        override fun randomMove(builder: FullMovesBuilder, possibleMoves: PossibleMoves): FullMove =
                randomFromSequence(builder, possibleMoves.sequencesWith3, 3)

    },
    SEQUENCE_2_WITH_PARTIAL_1 {

        override fun count(possibleMoves: PossibleMoves): Int =
                when {
                    possibleMoves.sequencesWith2.size < 1 -> 0
                    else -> (possibleMoves.sequencesWith2.size - 1) * possibleMoves.partialMoves.size
                }

        override fun randomMove(builder: FullMovesBuilder, possibleMoves: PossibleMoves): FullMove =
                randomFromSequenceAndPartial(builder, possibleMoves.sequencesWith2, possibleMoves.partialMoves, 2, 1)

    },
    SEQUENCE_1_WITH_SEQUENCE_1 {

        override fun count(possibleMoves: PossibleMoves): Int =
                when {
                    possibleMoves.sequencesWith1.size < 2 -> 0
                    else -> binomialCoefficient(possibleMoves.sequencesWith1.size, 2)
                }

        override fun randomMove(builder: FullMovesBuilder, possibleMoves: PossibleMoves): FullMove =
                randomFromSequences(builder, possibleMoves.sequencesWith1, possibleMoves.sequencesWith1, 1, 1)

    },
    SEQUENCE_1_WITH_PARTIAL_2 {

        override fun count(possibleMoves: PossibleMoves): Int =
                when {
                    possibleMoves.sequencesWith1.size < 2 -> 0
                    else -> (possibleMoves.sequencesWith1.size - 2) * binomialCoefficient(possibleMoves.partialMoves.size, 2)
                }

        override fun randomMove(builder: FullMovesBuilder, possibleMoves: PossibleMoves): FullMove =
                randomFromSequenceAndPartial(builder, possibleMoves.sequencesWith1, possibleMoves.partialMoves, 1, 2)

    };

}