package pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.board.Board
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMovesBuilder
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.SingleMove
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.AbstractMovesSearch
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.playout.PossibleMoves
import java.util.*

/**
 * Created by kkarolcz on 27.12.2017.
 */
abstract class AbstractMovesSearchDoubling(board: Board, currentPlayer: Player, dice: Dice)
    : AbstractMovesSearch(board, currentPlayer, dice) {

    private val dieValue: Byte = dice.first
    protected var diceLeft: Int = 4
        private set

    protected val possibleMoves = PossibleMoves()
    protected val initialFullMoveBuilder = FullMovesBuilder(dice)

    protected fun initialize() {
        findBarMoves()

        if (diceLeft > 0) {
            findBearOffPossibilities()

            findStandardPartialMoves()
            findStandardSequentialMoves()
        }
    }


    protected fun firstForBearingOffAfterMoves(vararg moves: SingleMove): SingleMove? {
        if (moves.isEmpty() && playerCheckers.canBearOff) {
            return firstForBearingOff(playerCheckers.homeTowersIndices(), dieValue)
        }

        val tempPlayerCheckers = playerCheckers.clone()
        moves.forEach { move -> tempPlayerCheckers.move(move) }

        return when (tempPlayerCheckers.canBearOff) {
            true -> firstForBearingOff(tempPlayerCheckers.homeTowersIndices(), dieValue)
            false -> null
        }
    }

    /**
     * @return false if bar move not possible
     */
    private fun findBarMoves() {
        if (playerCheckers.barCheckers > 0) {
            possibleMoves.barMove = findPartialBarMove(dieValue)
            if (possibleMoves.barMove == null) {
                diceLeft = 0
                return // No moves possible
            }

            diceLeft -= playerCheckers.barCheckers // Subtract number of required bar moves to save finding other moves later

            // Add all possible bar moves to the builder. Maximum number is 4 as there are 4 dice available
            for (i in 1..minOf(playerCheckers.barCheckers, 4)) {
                initialFullMoveBuilder.append(possibleMoves.barMove!!)
            }

            if (diceLeft > 0) {
                findBarSequentialMoves()
            }
        }
    }

    private fun findBearOffPossibilities() {
        possibleMoves.bearOffPossibleInitially = playerCheckers.canBearOff
        possibleMoves.bearOffPossibleConditionally = diceLeft == 4 && playerCheckers.numberOfNonHomeTowers <= 3
    }

    private fun findStandardPartialMoves() {
        for (tower in playerCheckers.towerIterator()) {
            val move = findStandardPartialMoveForTower(tower.index, dieValue)
            if (move != null) {
                for (i in 1..minOf(tower.checkers.toInt(), diceLeft)) {
                    possibleMoves.partialMoves.add(move.clone())
                }
            }
        }
    }

    private fun findBarSequentialMoves() {
        // No need to decrement dice like in findStandardSequentialMoves(). It was already done because bar moves exist
        findSequentialMoves(Collections.singleton(possibleMoves.barMove!!), possibleMoves.barSequentialMoves, diceLeft)
    }

    private fun findStandardSequentialMoves() {
        // We assume that one of the partial moves is used so dice are decremented
        findSequentialMoves(possibleMoves.partialMoves, possibleMoves.standardSequentialMoves, diceLeft - 1)
    }

    private fun findSequentialMoves(partialMoves: Iterable<SingleMove>, sequences: SequencesForPartialMoves, initialDiceLeft: Int) {
        if (initialDiceLeft == 0)
            return

        for (partialMove in partialMoves) {
            var diceLeft = initialDiceLeft
            var oldIndex: Byte
            var newIndex = partialMove.newIndex
            val sequenceForPartialMove = sequences.getOrCreateSequence(partialMove)

            while (diceLeft > 0) {
                oldIndex = newIndex
                newIndex = findMove(oldIndex, dieValue)
                diceLeft -= 1

                if (newIndex == BoardIndex.NO_INDEX) {
                    break
                }

                sequenceForPartialMove.add(SingleMove(oldIndex, newIndex))
            }
        }
    }


}