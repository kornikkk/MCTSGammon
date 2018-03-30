package pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.playout.dice4

import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMovesBuilder
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.playout.FullMovesType
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.playout.PossibleMoves
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.playout.RandomMovesHelper.randomFromPartial
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.playout.RandomMovesHelper.randomFromSequence
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.playout.RandomMovesHelper.randomFromSequenceAndPartial
import pl.kkarolcz.utils.binomialCoefficient

/**
 * Created by kkarolcz on 30.03.2018.
 */
enum class FullMoveTypeDice4Possible3 : FullMovesType {
    PARTIAL_3 {
        override fun count(possibleMoves: PossibleMoves): Int =
                when {
                    possibleMoves.partialMoves.size < 3 -> 0
                    else -> binomialCoefficient(possibleMoves.partialMoves.size, 3)
                }

        override fun randomMove(builder: FullMovesBuilder, possibleMoves: PossibleMoves): FullMove =
                randomFromPartial(builder, possibleMoves.partialMoves, 3)


    },
    SEQUENCE_2 {
        override fun count(possibleMoves: PossibleMoves): Int =
                possibleMoves.sequencesWith2.size

        override fun randomMove(builder: FullMovesBuilder, possibleMoves: PossibleMoves): FullMove =
                randomFromSequence(builder, possibleMoves.sequencesWith2, 2)

    },
    SEQUENCE_1_WITH_PARTIAL_1 {
        override fun count(possibleMoves: PossibleMoves): Int =
                when {
                    possibleMoves.sequencesWith1.size < 1 -> 0
                    else -> (possibleMoves.sequencesWith1.size - 1) * possibleMoves.partialMoves.size
                }

        override fun randomMove(builder: FullMovesBuilder, possibleMoves: PossibleMoves): FullMove =
                randomFromSequenceAndPartial(builder, possibleMoves.sequencesWith1, possibleMoves.partialMoves, 1, 1)

    }
}