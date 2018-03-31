package pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random.dice4

import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMovesBuilder
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.PossibleMovesDoubling
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random.FullMovesType
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random.RandomFullMovesDoublingUtils.randomFromPartial
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random.RandomFullMovesDoublingUtils.randomFromSequence
import pl.kkarolcz.utils.binomialCoefficient

/**
 * Created by kkarolcz on 30.03.2018.
 */
enum class FullMoveTypeDice4Possible2 : FullMovesType {
    PARTIAL_2 {
        override fun count(possibleMoves: PossibleMovesDoubling): Int =
                when {
                    possibleMoves.partialMoves.size < 2 -> 0
                    else -> binomialCoefficient(possibleMoves.partialMoves.size, 2)
                }

        override fun randomMove(builder: FullMovesBuilder, possibleMoves: PossibleMovesDoubling): FullMove =
                randomFromPartial(builder, possibleMoves.partialMoves, 2)


    },
    SEQUENCE_1 {
        override fun count(possibleMoves: PossibleMovesDoubling): Int =
                possibleMoves.sequencesWith1.size

        override fun randomMove(builder: FullMovesBuilder, possibleMoves: PossibleMovesDoubling): FullMove =
                randomFromSequence(builder, possibleMoves.sequencesWith1, 1)

    }
}