package pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.board.Board
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.AbstractMovesSearchDoubling
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random.dice1.FullMoveTypeDice1Possible1
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random.dice2.FullMoveTypeDice2Possible1
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random.dice2.FullMoveTypeDice2Possible2
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random.dice3.FullMoveTypeDice3Possible1
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random.dice3.FullMoveTypeDice3Possible2
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random.dice3.FullMoveTypeDice3Possible3
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random.dice4.FullMoveTypeDice4Possible1
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random.dice4.FullMoveTypeDice4Possible2
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random.dice4.FullMoveTypeDice4Possible3
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.random.dice4.FullMoveTypeDice4Possible4
import kotlin.reflect.KClass

/**
 * Created by kkarolcz on 27.12.2017.
 */

class RandomFullMoveSearchDoubling(board: Board, currentPlayer: Player, dice: Dice)
    : AbstractMovesSearchDoubling(board, currentPlayer, dice) {

    override fun findAllImpl() {
        initialize()

        when {
            diceLeft > 0 -> {
                val fullMoveType: FullMovesType? = when (diceLeft) {
                    4 -> randomMoveTypeDice4()
                    3 -> randomMoveTypeDice3()
                    2 -> randomMoveTypeDice2()
                    1 -> randomMoveTypeDice1()
                    else -> null
                }
                val randomMove = fullMoveType?.randomMove(initialFullMoveBuilder.clone(), possibleMoves)
                when {
                    randomMove != null -> fullMoves.add(randomMove)
                    else -> fullMoves.add(initialFullMoveBuilder.build())
                }
            }
            possibleMoves.barMove != null -> fullMoves.add(initialFullMoveBuilder.build())
        }
    }

    private fun randomMoveTypeDice4(): FullMovesType? {
        val possible4 = probabilityForType(FullMoveTypeDice4Possible4::class)
        if (possible4.movePossible)
            return possible4.randomMoveType()

        val possible3 = probabilityForType(FullMoveTypeDice4Possible3::class)
        if (possible3.movePossible)
            return possible3.randomMoveType()

        val possible2 = probabilityForType(FullMoveTypeDice4Possible2::class)
        if (possible2.movePossible)
            return possible2.randomMoveType()

        val possible1 = probabilityForType(FullMoveTypeDice4Possible1::class)
        if (possible1.movePossible)
            return possible1.randomMoveType()

        return null
    }

    private fun randomMoveTypeDice3(): FullMovesType? {
        val possible3 = probabilityForType(FullMoveTypeDice3Possible3::class)
        if (possible3.movePossible)
            return possible3.randomMoveType()

        val possible2 = probabilityForType(FullMoveTypeDice3Possible2::class)
        if (possible2.movePossible)
            return possible2.randomMoveType()

        val possible1 = probabilityForType(FullMoveTypeDice3Possible1::class)
        if (possible1.movePossible)
            return possible1.randomMoveType()

        return null
    }

    private fun randomMoveTypeDice2(): FullMovesType? {
        val possible2 = probabilityForType(FullMoveTypeDice2Possible2::class)
        if (possible2.movePossible)
            return possible2.randomMoveType()

        val possible1 = probabilityForType(FullMoveTypeDice2Possible1::class)
        if (possible1.movePossible)
            return possible1.randomMoveType()

        return null
    }

    private fun randomMoveTypeDice1(): FullMovesType? {
        val possible1 = probabilityForType(FullMoveTypeDice1Possible1::class)
        if (possible1.movePossible)
            return possible1.randomMoveType()

        return null
    }

    private fun <E> probabilityForType(fullMoveType: KClass<E>) where E : Enum<E>?, E : FullMovesType =
            MovesProbabilityForType(possibleMoves, fullMoveType.java)
}