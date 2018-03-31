package pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random.dice3

import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMovesBuilder
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.PossibleMovesDoubling
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random.FullMovesType
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random.RandomFullMovesDoublingUtils.randomFromBarSequence
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random.RandomFullMovesDoublingUtils.randomFromPartial
import pl.kkarolcz.utils.binomialCoefficient

/**
 * Created by kkarolcz on 30.03.2018.
 */
enum class FullMoveTypeDice3Possible1 : FullMovesType {
    BAR_SEQUENCE_1 {
        override fun count(possibleMoves: PossibleMovesDoubling): Int =
                possibleMoves.barSequencesWith1.size

        override fun randomMove(builder: FullMovesBuilder, possibleMoves: PossibleMovesDoubling): FullMove =
                randomFromBarSequence(builder, possibleMoves.barSequencesWith1, 1)
    },
    PARTIAL_1 {
        override fun count(possibleMoves: PossibleMovesDoubling): Int =
                when {
                    possibleMoves.partialMoves.size < 1 -> 0
                    else -> binomialCoefficient(possibleMoves.partialMoves.size, 1)
                }

        override fun randomMove(builder: FullMovesBuilder, possibleMoves: PossibleMovesDoubling): FullMove =
                randomFromPartial(builder, possibleMoves.partialMoves, 1)


    }
}