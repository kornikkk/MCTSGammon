package pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.playout.dice4

import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMovesBuilder
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.playout.FullMovesType
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.playout.PossibleMoves
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.playout.RandomMovesHelper.randomFromPartial
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.playout.RandomMovesHelper.randomFromSequence
import pl.kkarolcz.utils.binomialCoefficient

/**
 * Created by kkarolcz on 30.03.2018.
 */
enum class FullMoveTypeDice4Possible2 : FullMovesType {
    PARTIAL_2 {
        override fun count(possibleMoves: PossibleMoves): Int =
                when {
                    possibleMoves.partialMoves.size < 2 -> 0
                    else -> binomialCoefficient(possibleMoves.partialMoves.size, 2)
                }

        override fun randomMove(builder: FullMovesBuilder, possibleMoves: PossibleMoves): FullMove =
                randomFromPartial(builder, possibleMoves.partialMoves, 2)


    },
    SEQUENCE_1 {
        override fun count(possibleMoves: PossibleMoves): Int =
                possibleMoves.sequencesWith1.size

        override fun randomMove(builder: FullMovesBuilder, possibleMoves: PossibleMoves): FullMove =
                randomFromSequence(builder, possibleMoves.sequencesWith1, 1)

    }
}