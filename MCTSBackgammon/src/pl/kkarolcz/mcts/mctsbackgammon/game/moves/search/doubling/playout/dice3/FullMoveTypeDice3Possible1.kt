package pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.playout.dice3

import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMovesBuilder
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.playout.FullMovesType
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.playout.PossibleMoves
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.playout.RandomMovesHelper.randomFromBarSequence
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.playout.RandomMovesHelper.randomFromPartial
import pl.kkarolcz.utils.binomialCoefficient

/**
 * Created by kkarolcz on 30.03.2018.
 */
enum class FullMoveTypeDice3Possible1 : FullMovesType {
    BAR_SEQUENCE_1 {
        override fun count(possibleMoves: PossibleMoves): Int =
                possibleMoves.barSequencesWith1.size

        override fun randomMove(builder: FullMovesBuilder, possibleMoves: PossibleMoves): FullMove =
                randomFromBarSequence(builder, possibleMoves.barSequencesWith1, 1)
    },
    PARTIAL_1 {
        override fun count(possibleMoves: PossibleMoves): Int =
                when {
                    possibleMoves.partialMoves.size < 1 -> 0
                    else -> binomialCoefficient(possibleMoves.partialMoves.size, 1)
                }

        override fun randomMove(builder: FullMovesBuilder, possibleMoves: PossibleMoves): FullMove =
                randomFromPartial(builder, possibleMoves.partialMoves, 1)


    }
}